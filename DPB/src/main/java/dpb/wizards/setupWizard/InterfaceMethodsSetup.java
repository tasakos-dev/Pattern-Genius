package dpb.wizards.setupWizard;

import java.util.List;

import dpb.controller.IPatternManager;
import dpb.controller.PatternManager;
import dpb.model.Method;

public class InterfaceMethodsSetup extends MethodsSetup {
	private IPatternManager patternManager;

	public InterfaceMethodsSetup(String className) {
		super(className);
		this.patternManager = new PatternManager();
	}

	@Override
	protected List<Method> getElements(String name) {
		return patternManager.getInterfaceMethods(name);
	}

}