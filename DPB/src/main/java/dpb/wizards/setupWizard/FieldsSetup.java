package dpb.wizards.setupWizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import dpb.controller.IPatternManager;
import dpb.controller.PatternManager;

public class FieldsSetup extends WizardPage {
	private Table table;
	private String className;
	private IPatternManager patternManager;
	public FieldsSetup(String className) {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
		this.className = className;
		this.patternManager = new PatternManager();
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(10, 10, 564, 296);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		table = new Table(scrolledComposite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.CENTER);
		tblclmnNewColumn.setWidth(275);
		tblclmnNewColumn.setText("type");
		
		
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.CENTER);
		tblclmnNewColumn_1.setWidth(275);
		tblclmnNewColumn_1.setText("name");
		scrolledComposite.setContent(table);
		scrolledComposite.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		String[][] fields = patternManager.getClassFields(className);
		String[] fieldsName = new String[fields.length];
		String[] fieldsType = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fieldsType[i] = fields[i][0]; 
			fieldsName[i] = fields[i][1];
			
		}
		
		for (int i = 0; i < fields.length; i++) {
		      new TableItem(table, SWT.NONE);
		}
		
		
		
	    TableItem[] items = table.getItems();
	    for (int i = 0; i < items.length; i++) {
	      TableEditor editor = new TableEditor(table);
	      Text type = new Text(table, SWT.NONE);
	      type.setText(fieldsType[i]);
	      editor.grabHorizontal = true;
	      editor.setEditor(type, items[i], 0);
	      editor = new TableEditor(table);
	      Text name = new Text(table, SWT.NONE);
	      name.setText(fieldsName[i]);
	      editor.grabHorizontal = true;
	      editor.setEditor(name, items[i], 1);
	      editor = new TableEditor(table);
	      editor.horizontalAlignment = SWT.LEFT;
	    }
	}

}
