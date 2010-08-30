package pp.eclipse.cache;

import pp.eclipse.common.DefinedItem;
import pp.eclipse.common.DefiningFile;

public class Cache {
	public static <I extends DefinedItem, C extends DefiningFile<I>> CacheStrategy<I, C> none() {
		return new NoCacheStrategy<I, C>();
	}
}
