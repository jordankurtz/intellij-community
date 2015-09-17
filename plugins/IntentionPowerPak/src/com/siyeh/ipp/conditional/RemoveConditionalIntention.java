/*
 * Copyright 2003-2015 Dave Griffith, Bas Leijdekkers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.siyeh.ipp.conditional;

import com.intellij.psi.PsiConditionalExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.siyeh.ig.PsiReplacementUtil;
import com.siyeh.ig.psiutils.BoolUtils;
import com.siyeh.ipp.base.Intention;
import com.siyeh.ipp.base.PsiElementPredicate;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class RemoveConditionalIntention extends Intention {

  @Override
  @NotNull
  public PsiElementPredicate getElementPredicate() {
    return new RemoveConditionalPredicate();
  }

  @Override
  public void processIntention(PsiElement element) {
    final PsiConditionalExpression expression = (PsiConditionalExpression)element;
    final PsiExpression condition = expression.getCondition();
    final PsiExpression thenExpression = expression.getThenExpression();
    assert thenExpression != null;
    @NonNls final String thenExpressionText = thenExpression.getText();
    if ("true".equals(thenExpressionText)) {
      final String newExpression = condition.getText();
      PsiReplacementUtil.replaceExpression(expression, newExpression);
    }
    else {
      final String newExpression = BoolUtils.getNegatedExpressionText(condition);
      PsiReplacementUtil.replaceExpression(expression, newExpression);
    }
  }
}