package poc.fwk.modelmapper.test;

import java.util.List;

import lombok.Data;

@Data
public class PojoDest {

	private String skipped;

	private List<PojoDestEntry> entries;

	private PojoValue value;

	private PojoDestEntry destEntry;

}
