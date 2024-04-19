package com.github.mauricioaniche.ck.metric;

import com.github.mauricioaniche.ck.CKMethodResult;
import com.github.mauricioaniche.ck.util.MethodCounter;
import org.eclipse.jdt.core.dom.*;

import java.util.*;

public class MethodInvocationMethodCounter implements CKASTVisitor, MethodLevelMetric {
	private Map<String, MethodCounter.MethodInformation> methodInvocations = new HashMap<>();
	private String currentMethod = null;

	@Override
	public void visit(MethodDeclaration node) {
		this.currentMethod = node.getName().getIdentifier();
		methodInvocations.putIfAbsent(currentMethod, new MethodCounter().new MethodInformation(currentMethod));
	}

	@Override
	public void visit(MethodInvocation node) {
		if (currentMethod != null) {
			MethodCounter.MethodInformation info = methodInvocations.get(currentMethod);
			String methodName = node.getName().getIdentifier();
			Map<String, Integer> counts = info.getMethodInvocationsCounter();
			counts.put(methodName, counts.getOrDefault(methodName, 0) + 1);
		}
	}

	@Override
	public void setResult(CKMethodResult result) {
		List<MethodCounter.MethodInformation> infos = new ArrayList<>(methodInvocations.values());
		String formattedResult = MethodCounter.formatResult(infos);
		result.setMethodInvocationsCounter(formattedResult);
	}
}
