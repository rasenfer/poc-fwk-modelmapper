package poc.fwk.modelmapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeMaps {
	public static final String INCLUDE_LAZY_VALUES = "withLazy";
	public static final String EXCLUDE_LAZY_VALUES = "withoutLazy";
}
