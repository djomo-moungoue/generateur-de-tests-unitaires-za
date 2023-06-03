/**
 * 
 */
package com.util;

import java.util.regex.Pattern;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * @author SergeOliver
 *
 */
public class GInputVerifier extends InputVerifier{

	@Override
	public boolean verify(JComponent input) {
		JTextField tf = (JTextField)input;
		boolean bool = false;
		try {
			bool = Pattern.matches("^\\d+$", tf.getText().trim());
			return bool;
		}catch(NumberFormatException e) {
			GUtil.exceptionMessage(e.getClass().getSimpleName(), e.getMessage());
			return bool; 
		}
	}

}
