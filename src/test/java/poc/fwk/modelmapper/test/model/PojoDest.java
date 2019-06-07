package poc.fwk.modelmapper.test.model;

import java.util.List;

import lombok.Data;

@Data
public class PojoDest {

	private Integer id;

	private String skipped;

	private List<PojoDestEntry> entries;

	private PojoValue value;

	private PojoDestEntry destEntry;

}
