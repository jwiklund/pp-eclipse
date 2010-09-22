package pp.eclipse.open;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DbMapTest {

	Connection c;
	Map<String, List<Item>> target;
	Hasher hasher = new Hasher();

	@Before
	public void startDb() throws Exception {
		c = DriverManager.getConnection("jdbc:h2:mem:test");
		target = new DBMap(c);
	}

	@After
	public void stopDb() throws Exception {
		c.close();
	}

	@Test
	public void get_not_existing_should_return_null() throws Exception {
		assertNull(target.get(hasher.hash("test")));
	}

	@Test
	public void put_should_succed() throws Exception {
		target.put(hasher.hash("test"), Collections.<Item> emptyList());
	}

	@Test
	public void get_after_set_should_return_same_value() throws Exception 
	{
		Item item = new Item(ItemType.Content, "test", null, 1);
		String hash = hasher.hash("test");
		target.put(hash, Collections.singletonList(item));
		assertEquals(Collections.singletonList(item), target.get(hash));
	}
	
	@Test
	public void get_after_double_set_should_return_last_item() throws Exception
	{
		Item item1 = new Item(ItemType.Content, "test1", null, 1);
		Item item2 = new Item(ItemType.Content, "test2", null, 2);
		String hash = hasher.hash("test");
		target.put(hash, Collections.singletonList(item1));
		target.put(hash, Collections.singletonList(item2));
		assertEquals(Collections.singletonList(item2), target.get(hash));
	}
	
	@Test
	public void contains_before_get_should_return_false() throws Exception
	{
		String hash = hasher.hash("test");
		assertFalse(target.containsKey(hash));
	}
	
	@Test
	public void contains_after_get_Should_return_true() throws Exception
	{
		String hash = hasher.hash("test");
		target.put(hash, Collections.<Item>emptyList());
		assertTrue(target.containsKey(hash));
	}
}
