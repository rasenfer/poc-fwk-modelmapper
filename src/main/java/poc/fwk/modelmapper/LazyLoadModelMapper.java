package poc.fwk.modelmapper;

import java.lang.reflect.Type;
import java.util.stream.Stream;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.modelmapper.ModelMapper;

public class LazyLoadModelMapper extends ModelMapper {

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
			initializeValues(source);
			dest = super.map(source, destinationType);
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
			initializeValues(source);
			dest = super.map(source, destinationType);
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
			initializeValues(source);
			super.map(source, destination);
		} else if (TypeMaps.EXCLUDE_LAZY_VALUES.equals(typeMapName)) {
			super.map(source, destination);
		} else {
			super.map(source, destination, typeMapName);
		}
	}

	@SuppressWarnings("unchecked")
	private void initializeValues(Object source) {
		if (source != null) {
			if (Iterable.class.isInstance(source)) {
				Iterable.class.cast(source).forEach(this::initializeValues);
			} else if (source.getClass().isArray()) {
				Stream.of((Object[]) source).forEach(this::initializeValues);
			} else {
				ReflectionToStringBuilder.toString(source);
			}
		}
	}
}
