package by.protest.helper;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import by.protest.bot.domain.entity.CarItem;

public class SpecificationBuilder {
	
	public Specification<CarItem> whereRegistrationLike(String line) {
		return new Specification<CarItem>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<CarItem> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.get("registration"), "%" + line + "%");
			}
		};
	}

}
