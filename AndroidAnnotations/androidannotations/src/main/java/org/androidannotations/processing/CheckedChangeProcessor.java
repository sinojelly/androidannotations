package org.androidannotations.processing;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.rclass.IRClass;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

/**
 * 
 * @author Rostislav Chekan
 * 
 */
public class CheckedChangeProcessor extends AbstractListenerProcessor {

	public CheckedChangeProcessor(ProcessingEnvironment processingEnv, IRClass rClass) {
		super(processingEnv, rClass);
	}

	@Override
	public Class<? extends Annotation> getTarget() {
		return CheckedChange.class;
	}

	@Override
	protected void makeCall(JBlock listenerMethodBody, JInvocation call, TypeMirror returnType) {
		listenerMethodBody.add(call);
	}

	@Override
	protected void processParameters(JMethod listenerMethod, JInvocation call, List<? extends VariableElement> parameters) {
		JVar btnParam = listenerMethod.param(classes.COMPOUND_BUTTON, "buttonView");
		JVar isCheckedParam = listenerMethod.param(codeModel.BOOLEAN, "isChecked");
		boolean isCheckedParamExists = parameters.size() == 2;
		boolean btnParamExists = parameters.size() >= 1;

		if (btnParamExists) {
			call.arg(btnParam);
		}
		if (isCheckedParamExists) {
			call.arg(isCheckedParam);
		}
	}

	@Override
	protected JMethod createListenerMethod(JDefinedClass listenerAnonymousClass) {
		return listenerAnonymousClass.method(JMod.PUBLIC, codeModel.VOID, "onCheckedChanged");
	}

	@Override
	protected String getSetterName() {
		return "setOnCheckedChangeListener";
	}

	@Override
	protected JClass getListenerClass() {
		return holder.refClass("android.widget.CompoundButton.OnCheckedChangeListener");
	}

	@Override
	protected JExpression castWidget(JVar view) {
		return JExpr.cast(classes.COMPOUND_BUTTON, view);
	}
}
