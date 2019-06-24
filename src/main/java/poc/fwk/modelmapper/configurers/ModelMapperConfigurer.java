package poc.fwk.modelmapper.configurers;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(Integer.MAX_VALUE)
@ConditionalOnMissingBean(ModelMapperLazyConfigurer.class)
public class ModelMapperConfigurer {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		configure(modelMapper);
		return modelMapper;
	}

	protected void configure(ModelMapper modelMapper) {
		modelMapper.getConfiguration()
			.setDeepCopyEnabled(true)
			.setSkipNullEnabled(true);
	}

}
