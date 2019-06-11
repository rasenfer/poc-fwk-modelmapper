package poc.fwk.modelmapper;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.ErrorMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LazyLoadModelMapper extends ModelMapper {

	public static final String LAZY_MODEL_MAPPER_THREAD_NAME = "_LAZY";

	@Override
	public <D> D map(Object source, Class<D> destinationType) {
		D dest;
		if (Iterable.class.isInstance(source) || source != null && source.getClass().isArray()) {
			dest = map(source, destinationType, TypeMaps.EXCLUDE_LAZY_VALUES);
		} else {
			dest = map(source, destinationType, TypeMaps.INCLUDE_LAZY_VALUES);
		}
		return dest;
	}

	@Override
	public <D> D map(Object source, Class<D> destinationType, String typeMapName) {
		D dest;
		if (TypeMaps.INCLUDE_LAZY_VALUES.equals(typeMapName)) {
			dest = mapLazy(() -> super.map(source, destinationType));
		} else if (TypeMaps.EXCLUDE_LAZY_VALUES.equals(typeMapName)) {
			dest = super.map(source, destinationType);
		} else {
			dest = super.map(source, destinationType, typeMapName);
		}
		return dest;
	}

	@Override
	public <D> D map(Object source, Type destinationType) {
		D dest;
		if (Iterable.class.isInstance(source) || source != null && source.getClass().isArray()) {
			dest = map(source, destinationType, TypeMaps.EXCLUDE_LAZY_VALUES);
		} else {
			dest = map(source, destinationType, TypeMaps.INCLUDE_LAZY_VALUES);
		}
		return dest;
	}

	@Override
	public <D> D map(Object source, Type destinationType, String typeMapName) {
		D dest;
		if (TypeMaps.INCLUDE_LAZY_VALUES.equals(typeMapName)) {
			dest = mapLazy(() -> super.map(source, destinationType));
		} else if (TypeMaps.EXCLUDE_LAZY_VALUES.equals(typeMapName)) {
			dest = super.map(source, destinationType);
		} else {
			dest = super.map(source, destinationType, typeMapName);
		}
		return dest;
	}

	@Override
	public void map(Object source, Object destination) {
		if (Iterable.class.isInstance(source) || source != null && source.getClass().isArray()) {
			map(source, destination, TypeMaps.EXCLUDE_LAZY_VALUES);
		} else {
			map(source, destination, TypeMaps.INCLUDE_LAZY_VALUES);
		}
	}

	@Override
	public void map(Object source, Object destination, String typeMapName) {
		if (TypeMaps.INCLUDE_LAZY_VALUES.equals(typeMapName)) {
			mapLazy(() -> {
				super.map(source, destination);
				return null;
			});
		} else if (TypeMaps.EXCLUDE_LAZY_VALUES.equals(typeMapName)) {
			super.map(source, destination);
		} else {
			super.map(source, destination, typeMapName);
		}
	}

	private <D> D mapLazy(Supplier<D> supplier) {
		try {
			return CompletableFuture.supplyAsync(() -> {
				Thread currentThread = Thread.currentThread();
				currentThread.setName(currentThread.getName() + LAZY_MODEL_MAPPER_THREAD_NAME);
				return supplier.get();
			}).get();
		} catch (MappingException e) {
			throw e;
		} catch (Exception e) {
			throw new MappingException(Arrays.asList(new ErrorMessage(e.getMessage(), e)));
		}
	}
}
