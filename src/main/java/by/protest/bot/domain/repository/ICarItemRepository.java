package by.protest.bot.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import by.protest.bot.domain.entity.CarItem;

public interface ICarItemRepository extends CrudRepository<CarItem,Long>, JpaSpecificationExecutor<CarItem>{
	
	public List<CarItem> findByRegistration(String registration);

}
