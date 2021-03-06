/*
 * generated by Xtext 2.12.0
 */
package com.eclipsesource.workflow.dsl.formatting2

import com.eclipsesource.workflow.dsl.services.WorkflowGrammarAccess
import com.eclipsesource.workflow.dsl.workflow.Assertion
import com.eclipsesource.workflow.dsl.workflow.ProbabilityConfiguration
import com.eclipsesource.workflow.dsl.workflow.WorkflowConfiguration
import com.google.inject.Inject
import org.eclipse.xtext.formatting2.AbstractFormatter2
import org.eclipse.xtext.formatting2.IFormattableDocument

class WorkflowFormatter extends AbstractFormatter2 {

	@Inject extension WorkflowGrammarAccess access

	def dispatch void format(WorkflowConfiguration wfConfig, extension IFormattableDocument document) {
		wfConfig.regionFor.keyword(":").prepend[noSpace].append[oneSpace]
		wfConfig.regionFor.keyword(",").append[newLine]
		wfConfig.probConf.format
		wfConfig.probConf.interior[indent]
		wfConfig.assertions.head.interior[indent]

		wfConfig.regionFor.keyword("assertions").prepend[newLines = 2].append[newLine]
		wfConfig.assertions.head.prepend[indent]
		wfConfig.assertions.last.append[indent]

	}

	def dispatch void format(ProbabilityConfiguration probConf, extension IFormattableDocument document) {
		probConf.prepend[newLines=2]
		probConf.regionFor.keyword("low").prepend[newLine]
		probConf.regionFor.keyword("medium").prepend[newLine]
		probConf.regionFor.keyword("high").prepend[newLine]
	}

	def dispatch void format(Assertion assert, extension IFormattableDocument document) {
		assert.before.append[newLine]
		assert.after.prepend[newLine]
	}
}
