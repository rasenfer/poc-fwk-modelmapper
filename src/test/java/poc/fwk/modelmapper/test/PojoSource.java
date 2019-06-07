package poc.fwk.modelmapper.test;

import java.util.List;

import lombok.Data;

@Data
public class PojoSource {

	private String skipped;

	private PojoValue value;

	private PojoSourceEntry sourceEntry;

	private List<PojoSourceEntry> entries;

}
