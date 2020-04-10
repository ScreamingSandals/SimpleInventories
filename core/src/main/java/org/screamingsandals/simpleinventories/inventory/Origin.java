package org.screamingsandals.simpleinventories.inventory;

import java.io.File;
import java.util.List;

import org.screamingsandals.simpleinventories.builder.FormatBuilder;

public class Origin {

	private File file = null;
	private List<Object> content;
	private Type type;
	private FormatBuilder builder = null;
	
	public Origin(List<Object> content) {
		this.content = content;
		this.type = Type.INTERNAL;
	}
	
	public Origin(File file, List<Object> content) {
		this.file = file;
		this.content = content;
		this.type = Type.FILE;
	}
	
	public Origin(FormatBuilder builder, List<Object> content) {
		this.builder = builder;
		this.content = content;
		this.type = Type.BUILDER;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public FormatBuilder getBuilder() {
		return this.builder;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public List<Object> getContent() {
		return this.content;
	}
	
	public static enum Type {
		FILE,
		BUILDER,
		INTERNAL;
	}

}
