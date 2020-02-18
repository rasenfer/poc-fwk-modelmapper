package poc.fwk.modelmapper;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.ErrorMessage;

import lombok.Getter;
import lombok.Setter;

public class LazyLoadModelMapper extends ModelMapper {

	private final ThreadLocal<Boolean> lazyInclude = new ThreadLocal<>();

	private final Set<String> internalTypeMaps = new HashSet<>(Arrays.asList(
			TypeMaps.INCLUDE_LAZY_VALUES, TypeMaps.EXCLUDE_LAZY_VALUES));

	@Getter
	@Setter
	private String defaultObjectTypeMap = TypeMaps.INCLUDE_LAZY_VALUES;

	@Getter
	@Setter
	private String defaultIterableTypeMap = TypeMaps.EXCLUDE_LAZY_VALUES;

	@Override
	public <D> D map(Object source, Class<D> destinationType) {
		D dest;
		if (Iterable.class.isInstance(source) || source != null && source.getClass().isArray()) {
			dest = map(source, destinationType, defaultIterableTypeMap);
		} else {
			dest = map(source, destinationType, defaultObjectTypeMap);
		}
		return dest;
	}

	@Override
	public <D> D map(Object source, Class<D> destinationType, String typeMapName) {
		D dest;
		if (internalTypeMaps.contains(typeMapName)) {
			dest = map(() -> super.map(source, destinationType), typeMapName);
		} else {
			dest = map(() -> super.map(source, destinationType, typeMapName), TypeMaps.EXCLUDE_LAZY_VALUES);
		}
		return dest;
	}

	@Override
	public <D> D map(Object source, Type destinationType) {
		D dest;
		if (Iterable.class.isInstance(source) || source != null && source.getClass().isArray()) {
			dest = map(source, destinationType, defaultIterableTypeMap);
		} else {
			dest = map(source, destinationType, defaultObjectTypeMap);
		}
		return dest;
	}

	@Override
	public <D> D map(Object source, Type destinationType, String typeMapName) {
		D dest;
		if (internalTypeMaps.contains(typeMapName)) {
			dest = map(() -> super.map(source, destinationType), typeMapName);
		} else {
			dest = map(() -> super.map(source, destinationType, typeMapName), TypeMaps.EXCLUDE_LAZY_VALUES);
		}
		return dest;
	}

	@Override
	public void map(Object source, Object destination) {
		if (Iterable.class.isInstance(source) || source != null && source.getClass().isArray()) {
			map(source, destination, defaultIterableTypeMap);
		} else {
			map(source, destination, defaultObjectTypeMap);
		}
	}

	@Override
	public void map(Object source, Object destination, String typeMapName) {
		if (internalTypeMaps.contains(typeMapName)) {
			map(() -> {
				super.map(source, destination);
				return null;
			}, typeMapName);
		} else {
			map(() -> {
				super.map(source, destination, typeMapName);
				return null;
			}, TypeMaps.EXCLUDE_LAZY_VALUES);
		}
	}

	private <D> D map(Supplier<D> supplier, String internalTypeMap) {
		Boolean includeLazy = TypeMaps.INCLUDE_LAZY_VALUES.equals(internalTypeMap);
		try {
			return CompletableFuture.supplyAsync(() -> {
				lazyInclude.set(includeLazy);
				D mapped = supplier.get();
				lazyInclude.remove();
				return mapped;
			}).get();
		} catch (MappingException e) {
			throw e;
		} catch (Exception e) {
			throw new MappingException(Arrays.asList(new ErrorMessage(e.getMessage(), e)));
		}
	}

	public boolean getLazyInclude() {
		return lazyInclude.get() != null ? lazyInclude.get() : Boolean.FALSE;
	}
}
