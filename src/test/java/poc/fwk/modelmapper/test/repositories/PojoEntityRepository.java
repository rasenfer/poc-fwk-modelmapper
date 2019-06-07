package poc.fwk.modelmapper.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poc.fwk.modelmapper.test.entities.PojoEntity;

@Repository
public interface PojoEntityRepository extends JpaRepository<PojoEntity, Integer> {

}
