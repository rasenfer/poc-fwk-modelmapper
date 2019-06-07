package poc.fwk.modelmapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import poc.fwk.modelmapper.configurers.ModelMapperConfigurer;
import poc.fwk.modelmapper.configurers.ModelMapperLazyConfigurer;
import poc.fwk.modelmapper.test.model.PojoDest;
import poc.fwk.modelmapper.test.model.PojoDestEntry;
import poc.fwk.modelmapper.test.model.PojoSource;
import poc.fwk.modelmapper.test.model.PojoSourceEntry;
import poc.fwk.modelmapper.test.model.PojoValue;
import poc.fwk.test.SpringTestContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestContext.class)
public class ModelMapperTest {

	@Autowired
	private ModelMapperConfigurer modelMapperConfigurer;

	@Autowired
	private ModelMapperLazyConfigurer modelMapperLazySkipModuleConfigurer;

	@Autowired
	private ModelMapper modelMapper;

	@Before
	public void setUp() {
		modelMapper.typeMap(PojoSource.class, PojoDest.class)
				// map to another property with conversion
				.addMapping(PojoSource::getSourceEntry, PojoDest::setDestEntry)
				// skip mapping
				.addMappings(mapper -> mapper.skip(PojoDest::setSkipped));
	}

	@Test
	public void testContext() {
		assertNotNull(modelMapper);
		assertNotNull(modelMapperConfigurer);
		assertNotNull(modelMapperLazySkipModuleConfigurer);
	}

	@Test
	public void testMap() {
		PojoSource source = getPojoSource();
		PojoDest dest = modelMapper.map(source, PojoDest.class);
		asserPojoMap(source, dest);
	}

	@Test
	public void testMapList() {
		PojoSource source = getPojoSource();
		List<PojoDest> dest = modelMapper.map(Arrays.asList(source), new TypeToken<List<PojoDest>>() {}.getType());
		asserPojoMap(source, dest.get(0));
	}

	@Test
	public void testCopy() {
		PojoSource source = getPojoSource();
		PojoSource dest = modelMapper.map(source, PojoSource.class);
		assertNotSame(source, dest);
	}

	private PojoSource getPojoSource() {
		PojoSource source = new PojoSource();
		source.setSkipped("skipped");

		PojoValue value = new PojoValue();
		value.setId(1);
		value.setValue("value");
		source.setValue(value);

		PojoSourceEntry entry = new PojoSourceEntry();
		entry.setId(11);
		entry.setValue("entry");
		source.setEntries(Arrays.asList(entry));

		source.setSourceEntry(entry);

		return source;
	}

	private void asserPojoMap(PojoSource source, PojoDest dest) {
		// check skipped property
		assertNull(dest.getSkipped());
		assertEquals(source.getId(), dest.getId());
		// check distinct property mapping with conversion
		assertEquals(source.getSourceEntry().getId(), dest.getDestEntry().getId());
		assertEquals(source.getSourceEntry().getValue(), dest.getDestEntry().getValue());
		// check array entries properties with conversion
		assertTrue(PojoDestEntry.class.isInstance(dest.getEntries().get(0)));
		assertEquals(source.getEntries().get(0).getId(), dest.getEntries().get(0).getId());
		assertEquals(source.getEntries().get(0).getValue(), dest.getEntries().get(0).getValue());
		// check same property class mapping must not be same instance
		assertNotSame(source.getValue(), dest.getValue());
		assertEquals(source.getValue(), dest.getValue());
	}
}
