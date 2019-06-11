package poc.fwk.modelmapper.test.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import poc.fwk.modelmapper.TypeMaps;
import poc.fwk.modelmapper.test.model.PojoDest;
import poc.fwk.modelmapper.test.repositories.PojoEntityRepository;

@Service
@RequiredArgsConstructor
public class PojoEntityServiceImpl implements PojoEntityService {

	@Autowired
	private final PojoEntityRepository pojoEntityRepository;

	@Autowired
	private final ModelMapper modelMapper;

	@Override
	public List<PojoDest> getEntitiesWithLazyValues() {
		return modelMapper.map(pojoEntityRepository.findAll(), new TypeToken<List<PojoDest>>() {}.getType(),
				TypeMaps.INCLUDE_LAZY_VALUES);
	}

	@Override
	public List<PojoDest> getEntitiesWithoutLazyValues() {
		return modelMapper.map(pojoEntityRepository.findAll(), new TypeToken<List<PojoDest>>() {}.getType());
	}

	@Override
	public PojoDest getEntityWithLazyValues(Integer id) {
		return modelMapper.map(pojoEntityRepository.getOne(id), PojoDest.class);
	}

	@Override
	public PojoDest getEntityWithoutLazyValues(Integer id) {
		return modelMapper.map(pojoEntityRepository.getOne(id), PojoDest.class,
				TypeMaps.EXCLUDE_LAZY_VALUES);
	}
}
