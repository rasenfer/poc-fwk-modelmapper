package poc.fwk.modelmapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import poc.fwk.modelmapper.test.entities.PojoEntity;
import poc.fwk.modelmapper.test.entities.PojoEntityElement;
import poc.fwk.modelmapper.test.entities.PojoEntityEntry;
import poc.fwk.modelmapper.test.model.PojoDest;
import poc.fwk.modelmapper.test.repositories.PojoEntityRepository;
import poc.fwk.modelmapper.test.services.PojoEntityService;
import poc.fwk.test.SpringTestContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestContext.class)
public class ModelMapperLazyTest {

	@Autowired
	private PojoEntityRepository pojoEntityRepository;

	@Autowired
	private PojoEntityService pojoEntityService;

	@Autowired
	private ModelMapper modelMapper;

	private Integer id;

	@Before
	public void setUp() {
		modelMapper.typeMap(PojoEntity.class, PojoDest.class)
				// map to another property with conversion
				.addMapping(PojoEntity::getEntry, PojoDest::setDestEntry)
				// skip mapping
				.addMappings(mapper -> mapper.skip(PojoDest::setSkipped));
		id = pojoEntityRepository.save(getPojoEntity()).getId();
	}

	@Test
	@DirtiesContext
	public void testMapEntitiesWithLazyValues() {
		PojoDest dest = pojoEntityService.getEntitiesWithLazyValues().get(0);
		assertNotNull(dest.getDestEntry());
		assertFalse(CollectionUtils.isEmpty(dest.getEntries()));
	}

	@Test
	@DirtiesContext
	public void testMapEntitiesWithoutLazyValues() {
		PojoDest dest = pojoEntityService.getEntitiesWithoutLazyValues().get(0);
		assertNull(dest.getDestEntry());
		assertTrue(CollectionUtils.isEmpty(dest.getEntries()));
	}

	@Test
	@DirtiesContext
	public void testMapEntityWithLazyValues() {
		PojoDest dest = pojoEntityService.getEntityWithLazyValues(id);
		assertNotNull(dest.getDestEntry());
		assertFalse(CollectionUtils.isEmpty(dest.getEntries()));
	}

	@Test
	@DirtiesContext
	public void testMapEntityWithoutLazyValues() {
		PojoDest dest = pojoEntityService.getEntityWithoutLazyValues(id);
		assertNull(dest.getDestEntry());
		assertTrue(CollectionUtils.isEmpty(dest.getEntries()));
	}

	private PojoEntity getPojoEntity() {
		PojoEntity source = new PojoEntity();

		PojoEntityEntry entry = new PojoEntityEntry();
		entry.setValue("entry");
		source.setEntries(Arrays.asList(entry));

		PojoEntityElement element = new PojoEntityElement();
		element.setValue("element");
		source.setEntry(element);

		return source;
	}
}
