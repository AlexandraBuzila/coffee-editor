package com.eclipsesource.demo.rest;

import java.io.IOException;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.osgi.service.component.annotations.Component;
import com.eclipsesource.workflow.analyzer.json.AnalyzeWorkflow;
import com.eclipsesource.workflow.generator.IWorkflowGeneratorInput;
import com.eclipsesource.workflow.generator.IWorkflowGeneratorOutput;
import com.eclipsesource.workflow.generator.WorkflowGeneratorInputFactory;
import com.eclipsesource.workflow.generator.java.JavaWorkflowGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/backend")
@Component(service = StaticContentService.class)
public class StaticContentService {

	public StaticContentService() {
		System.out.println("STARTED");
	}

//	@Path("/wfanalysis")
//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public String workflowAnalysis(String json) {
//		JsonElement jsonElement = new JsonParser().parse(json);
//		
//		//call something to parse the json?
//		
//		return new WorkflowAnalysis(jsonElement).generateAnalysisDataAsJson(new NullProgressMonitor());
//	}

	@Path("/wfanalysis")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String workflowAnalysis(String json) {
		JsonObject element = new JsonParser().parse(json).getAsJsonObject();
		try {
			return new AnalyzeWorkflow(element.get("graph").toString(), element.get("config").getAsString()).generate(new NullProgressMonitor());
		} catch (IOException e) {
			return new Gson().toJson(e);
		}
	}

	@Path("/generate")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String codeGeneration(String json) {
		JsonElement element = new JsonParser().parse(json);
		String packageName = element.getAsJsonObject().get("packageName").getAsString();
		String sourceFile = element.getAsJsonObject().get("sourceFile").getAsString();
		String content = element.getAsJsonObject().get("content").getAsJsonObject().toString();
		Optional<IWorkflowGeneratorInput> createInput = WorkflowGeneratorInputFactory.getInstance()
				.createInput(packageName, sourceFile, content);
		IWorkflowGeneratorOutput generateClasses = new JavaWorkflowGenerator().generateClasses(createInput.get(),
				new NullProgressMonitor());
		return new Gson().toJson(generateClasses.getGeneratedFiles());
	}
}
