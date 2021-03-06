package poc.fwk.modelmapper.configurers;

import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;
import poc.fwk.modelmapper.LazyLoadModelMapper;

@Configuration
@Order(Integer.MAX_VALUE - 1)
@ConditionalOnClass(HibernateProxy.class)
public class ModelMapperLazyConfigurer extends ModelMapperConfigurer {

	@Bean
	@Override
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new LazyLoadModelMapper();
		configure(modelMapper);
		return modelMapper;
	}

	@Override
	protected void configure(ModelMapper modelMapper) {
		super.configure(modelMapper);
		modelMapper.getConfiguration()
				.setPropertyCondition(condition -> {
					Object value = condition.getSource();
					boolean map = true;
					if (value != null && LazyLoadModelMapper.class.isInstance(modelMapper)
							&& !LazyLoadModelMapper.class.cast(modelMapper).getLazyInclude()) {
						if (ProxyFactory.isProxyClass(value.getClass())) {
							MethodHandler handler = ProxyFactory.getHandler(Proxy.class.cast(value));
							if (JavassistLazyInitializer.class.isInstance(handler)) {
								map = !JavassistLazyInitializer.class.cast(handler).isUninitialized();
							}
						}
						map = map && (!HibernateProxy.class.isInstance(value)
								|| !HibernateProxy.class.cast(value).getHibernateLazyInitializer().isUninitialized());
						map = map && (!PersistentCollection.class.isInstance(value)
								|| PersistentCollection.class.cast(value).wasInitialized());
					}
					return map;
				});
	}
}
