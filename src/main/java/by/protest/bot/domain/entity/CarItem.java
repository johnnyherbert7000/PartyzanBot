package by.protest.bot.domain.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="car_items")
public class CarItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
	private Long id;
	@Column(name="registration", nullable=false)
	private String registration;
	@Column (name="mark1")
	private String mark1;
	@Column (name="mark2")
	private String mark2;
	@Column (name="description")
	private String description;
	@Column (name="source")
	private String source;
	
	public CarItem() {}
	
	
	public CarItem(String registration, String description) {
		super();
		this.registration = registration;
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRegistration() {
		return registration;
	}
	public void setRegistration(String registration) {
		this.registration = registration;
	}
	public String getColor() {
		return mark1;
	}
	public void setColor(String color) {
		this.mark1 = color;
	}
	public String getMark() {
		return mark2;
	}
	public void setMark(String mark) {
		this.mark2 = mark;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	

}
