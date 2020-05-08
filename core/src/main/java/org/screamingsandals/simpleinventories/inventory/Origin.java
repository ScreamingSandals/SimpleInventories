package org.screamingsandals.simpleinventories.inventory;

import java.io.File;
import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.builder.FormatBuilder;
import org.screamingsandals.simpleinventories.item.BuyCallback;
import org.screamingsandals.simpleinventories.item.PostClickCallback;
import org.screamingsandals.simpleinventories.item.PreClickCallback;
import org.screamingsandals.simpleinventories.item.RenderCallback;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Origin {

	private final File file;
	private final List<Object> content;
	private final Type type;
	private final FormatBuilder builder;

	private List<OpenCallback> openCallbacks = Collections.emptyList();
	private List<RenderCallback> renderCallbacks = Collections.emptyList();
	private List<PreClickCallback> preClickCallbacks = Collections.emptyList();
	private List<BuyCallback> buyCallbacks = Collections.emptyList();
	private List<PostClickCallback> postClickCallbacks = Collections.emptyList();
	private List<CloseCallback> closeCallbacks = Collections.emptyList();
	
	public Origin(List<Object> content) {
		this(null, content, Type.INTERNAL, null);
	}
	
	public Origin(File file, List<Object> content) {
		this(file, content, Type.FILE, null);
	}
	
	public Origin(FormatBuilder builder, List<Object> content) {
		this(null, content, Type.BUILDER, builder);
	}
	
	public static enum Type {
		FILE,
		BUILDER,
		INTERNAL;
	}

}
