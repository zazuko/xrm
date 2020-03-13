package com.zazuko.rdfmapping.dsl.common.components;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowComponent;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;

public class StringReplacingComponent implements IWorkflowComponent {

	private String fileName;
	private String oldValue;
	private String newValue;

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	@Override
	public void preInvoke() {

	}

	@Override
	public void invoke(IWorkflowContext ctx) {
		try {
			Path path = Paths.get(fileName);
			String pluginXml = new String(Files.readAllBytes(path));
			pluginXml = pluginXml.replace(this.oldValue, this.newValue);
			Files.write(path, pluginXml.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void postInvoke() {

	}

}
