package dpb.wizards.mainWizard;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;



import dpb.wizards.setupWizard.SetupWizard;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class PatternMainSetupPage extends WizardPage {
	private ISelection selection;

	public PatternMainSetupPage(ISelection selection) {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(10, 56, 432, 247);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Tree tree = new Tree(scrolledComposite, SWT.BORDER);
		
		
		TreeItem classesTreeItem = new TreeItem(tree, SWT.NONE);
		classesTreeItem.setText("Classes");
		
		TreeItem interfacesTreeItem = new TreeItem(tree, SWT.NONE);
		interfacesTreeItem.setText("Interfaces");
		
		TreeItem trtmNewTreeitem_1 = new TreeItem(classesTreeItem, SWT.NONE);
		trtmNewTreeitem_1.setText("Singleton");
		classesTreeItem.setExpanded(true);
		scrolledComposite.setContent(tree);
		scrolledComposite.setMinSize(tree.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		Button editClassBtn = new Button(container, SWT.NONE);
		
		editClassBtn.setBounds(448, 56, 99, 32);
		editClassBtn.setText("Edit class");
		editClassBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.err.println("click");
				WizardDialog wizardDialog = new WizardDialog(getShell(), new SetupWizard());
				wizardDialog.open();
		
			}
		});
		
		Button editInterfaceBtn = new Button(container, SWT.NONE);
		editInterfaceBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		editInterfaceBtn.setBounds(448, 94, 99, 32);
		editInterfaceBtn.setText("Edit interface");

	}
}
