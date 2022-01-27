package by.protest.bot.domain.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import by.protest.bot.domain.entity.SourceItem;

public interface ISourceItemRepository extends CrudRepository<SourceItem, Long> {
	public List<SourceItem> findByAnchor(String anchor);
}
