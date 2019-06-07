package poc.fwk.modelmapper.test.model;

import java.util.List;

import lombok.Data;

@Data
public class PojoSource {

	private Integer id;

	private String skipped;

	private PojoValue value;

	private PojoSourceEntry sourceEntry;

	private List<PojoSourceEntry> entries;

}
