package pp.eclipse.open;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class DBMap extends AbstractMap<String, List<Item>> 
{
	private final Connection c;

	public DBMap(Connection connection) throws SQLException 
	{
		this.c = connection;
		create();
	}
	
	void create() throws SQLException {
		Statement s = c.createStatement();
		try {
			s.execute("create table if not exists hash_items( hash varchar(40) primary key, data blob )");
		} finally {
			s.close();
		}
	}
	
	@Override
	public List<Item> get(final Object key) {
		if (!(key instanceof String)) {
			return null;
		}
		return new DBRunner<List<Item>>(c) {
			public List<Item> run(Connection c) throws SQLException {
				ps = c.prepareStatement("select data from hash_items where hash = ?");
				ps.setString(1, (String) key);
				rs = ps.executeQuery();
				if (rs.next()) {
					return read(rs.getBinaryStream(1));
				}
				return null;
			}
		}.doRun();
	}
	
	@Override
	public boolean containsKey(final Object key) {
		if (!(key instanceof String)) {
			return false;
		}
		Boolean res = new DBRunner<Boolean>(c) {
			@Override
			public Boolean run(Connection c) throws SQLException {
				ps = c.prepareStatement("select count(1) from hash_items where hash = ?");
				ps.setString(1, (String) key);
				rs = ps.executeQuery();
				if (rs.next()) {
					return 1 == rs.getInt(1);
				}
				return false;
			}
		}.doRun();
		return res != null && res;
	}

	@Override
	public List<Item> put(final String key, final List<Item> value) {
		Boolean ranOk = new DBRunner<Boolean>(c) {
			public Boolean run(Connection c) throws SQLException {
				ps = c.prepareStatement("insert into hash_items(hash, data) values (?, ?)");
				ps.setString(1, (String) key);
				ps.setBlob(2, write(value));
				try {
					ps.executeUpdate();
				} catch (Exception e) {
					// Assume element already exists, should be uncommon
					return false;
				}
				return true;
			}
		}.doRun();
		if (ranOk != null && ranOk) {
			return null;
		}
		new DBRunner<Void>(c) {
			public Void run(Connection c) throws SQLException {
				ps = c.prepareStatement("update hash_items set data = ? where hash = ?");
				ps.setBlob(1, write(value));
				ps.setString(2, (String) key);
				ps.executeUpdate();
				return null;
			}
		}.doRun();
		return null;
	}
	
	private List<Item> read(InputStream binaryStream) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(binaryStream, "UTF8"));
			int count = Integer.parseInt(br.readLine());
			List<Item> result = new ArrayList<Item>(count);
			for (int i = 0 ; i < count ; i++) {
				ItemType type = null;
				String tmp = br.readLine();
				if (tmp.length() > 0) {
					type = ItemType.valueOf(tmp);
				}
				String externalid = null;
				tmp = br.readLine();
				if (tmp.length() > 0) {
					externalid = tmp;
				}
				int line = -1;
				tmp = br.readLine();
				if (tmp.length() > 0) {
					line = Integer.valueOf(tmp);
				}
				IPath path = null;
				tmp = br.readLine();
				if (tmp.length() > 0) {
					path = Path.fromPortableString(tmp);
				}
				Item item = new Item(type, externalid, path, line);
				result.add(item);
			}
			return result;
		} catch (Exception e) {
			Logger.getLogger(DBMap.class.getName()).log(Level.FINE, "read failed: " + e.getMessage());
			Logger.getLogger(DBMap.class.getName()).log(Level.FINER, "read failed", e);
		}
		return null;
	}
	
	private InputStream write(List<Item> value) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(output, "UTF8"));
			pw.println(value.size());
			for (Item item : value) {
				if (item.type() == null) {
					pw.println();
				} else {
					pw.println(item.type());
				}
				if (item.externalid() == null) {
					pw.println();
				} else {
					pw.println(item.externalid());
				}
				if (item.line() == -1) {
					pw.println();
				} else {
					pw.println(item.line());
				}
				if (item.path() == null) {
					pw.println();
				} else {
					pw.println(item.path().toPortableString());
				}
			}
			pw.flush();
			pw.close();
			return new ByteArrayInputStream(output.toByteArray());
		} catch (Exception e) {
			Logger.getLogger(DBMap.class.getName()).log(Level.FINE, "write failed: " + e.getMessage());
			Logger.getLogger(DBMap.class.getName()).log(Level.FINER, "write failed", e);
		}
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<String, List<Item>>> entrySet() {
		throw new UnsupportedOperationException();
	}

	public static abstract class DBRunner<R> {
		private final Connection c;
		protected PreparedStatement ps;
		protected ResultSet rs;

		public DBRunner(Connection c) {
			this.c = c;
		}

		public abstract R run(Connection c) throws SQLException ;
		
		public final R doRun() {
			try {
				return run(c);
			} catch (SQLException e) {
				Logger.getLogger(DBMap.class.getName()).log(Level.FINE, "sql op failed: " + e.getMessage());
				Logger.getLogger(DBMap.class.getName()).log(Level.FINER, "sql op failed", e);
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						// ignore
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						// ignore
					}
				}
			}
			return null;
		}
	}
}
