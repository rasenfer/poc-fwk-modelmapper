package poc.fwk.modelmapper.test.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class PojoEntityElement {

	@Id
	@GeneratedValue
	private Integer id;

	@Column
	private String value;
}
