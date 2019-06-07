package poc.fwk.modelmapper.test.services;

import java.util.List;

import poc.fwk.modelmapper.test.model.PojoDest;

public interface PojoEntityService {

	PojoDest getEntityWithLazyValues(Integer id);

	PojoDest getEntityWithoutLazyValues(Integer id);

	List<PojoDest> getEntitiesWithLazyValues();

	List<PojoDest> getEntitiesWithoutLazyValues();
}
