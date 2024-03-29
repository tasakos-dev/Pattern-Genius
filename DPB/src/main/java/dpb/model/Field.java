package dpb.model;

public class Field {
	private String name;
	private String type;
	private String modifier;
	private boolean isStatic;
	
	public Field(String name, String type, String modifier, boolean isStatic) {
		super();
		this.name = name;
		this.type = type;
		this.modifier = modifier;
		this.isStatic = isStatic;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public String getModifier() {
		return modifier;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	
	
	public boolean isStatic() {
		return isStatic;
	}
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Field other = (Field) obj;
		if (modifier == null) {
			if (other.modifier != null)
				return false;
		} else if (!modifier.equals(other.modifier))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Field [name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", modifier=");
		builder.append(modifier);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	

}
