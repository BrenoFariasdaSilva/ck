package com.github.mauricioaniche.ck.util;

import java.util.*;
import java.util.stream.Collectors;

public class MethodCounter {
	public class MethodInformation {
		private String parentName;
		private LinkedHashMap<String, Integer> methodInvocations = new LinkedHashMap<>();

		public MethodInformation(String parentName) {
			this.parentName = parentName;
		}

		public String getParentName() {
			return parentName;
		}

		public Map<String, Integer> getMethodInvocations() {
			return methodInvocations;
		}

		@Override
		public String toString() {
			return "MethodInformation{" +
					"parentName='" + parentName + '\'' +
					", methodInvocation=" + methodInvocations +
					'}';
		}

		public String toFormattedString() {
			return parentName + "[ " + formatMethods(methodInvocations) + " ] ";
		}

		private Map<String, Integer> sortMethods(Map<String, Integer> methodInvocation) {
			return methodInvocation.entrySet().stream()
					.sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		}

		private String formatMethods(Map<String, Integer> methodInvocation) {
			return methodInvocation.entrySet().stream()
					.map(entry -> entry.getKey() + "()" + ":" + entry.getValue())
					.collect(Collectors.joining(" "));
		}

	}

	public static String formatResult(List<MethodInformation> methodInformations) {
		return methodInformations.stream().map(MethodInformation::toFormattedString).collect(Collectors.joining());
	}
	public static List<MethodInformation> count(String methodList) {
		List<MethodInformation> methodInformationList = new ArrayList<>();

		String[] methodNames = methodList.split(";");
		for (String name : methodNames) {
			String[] parts = name.split("/");
			String methodName = parts[0];
			String parentName = parts[1];

			MethodInformation methodInformation = new MethodCounter().new MethodInformation(parentName);
			if (methodInformationList.contains(methodInformation)) {
				methodInformation = methodInformationList.get(methodInformationList.indexOf(methodInformation));
			} else {
				methodInformationList.add(methodInformation);
			}

			if (methodInformation.getMethodInvocations().containsKey(methodName)) {
				methodInformation.getMethodInvocations().put(methodName, methodInformation.getMethodInvocations().get(methodName) + 1);
			} else {
				methodInformation.getMethodInvocations().put(methodName, 1);
			}
		}

		return methodInformationList;
	}
}
