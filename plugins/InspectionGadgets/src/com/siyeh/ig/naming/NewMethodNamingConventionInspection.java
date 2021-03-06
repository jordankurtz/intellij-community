/*
 * Copyright 2000-2017 JetBrains s.r.o.
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
package com.siyeh.ig.naming;

import com.intellij.codeInspection.NamingConvention;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.psiutils.LibraryUtil;
import com.siyeh.ig.psiutils.MethodUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class NewMethodNamingConventionInspection extends AbstractNamingConventionInspection<PsiMethod> {
  public static final ExtensionPointName<NamingConvention<PsiMethod>> EP_NAME = ExtensionPointName.create("com.intellij.naming.convention.method");
  public NewMethodNamingConventionInspection() {
    super(EP_NAME.getExtensions(), InstanceMethodNamingConvention.INSTANCE_METHOD_NAMING_CONVENTION);
  }

  @Override
  public boolean shouldInspect(PsiFile file) {
    return file instanceof PsiClassOwner;
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return "Method naming convention";
  }

  @Override
  public BaseInspectionVisitor buildVisitor() {
    return new BaseInspectionVisitor() {
      @Override
      public void visitMethod(PsiMethod method) {
        if (method.isConstructor()) {
          return;
        }

        if (!isOnTheFly() && MethodUtils.hasSuper(method)) {
          return;
        }

        if (LibraryUtil.isOverrideOfLibraryMethod(method)) {
          return;
        }

        String name = method.getName();
        checkName(method, name, shortName -> registerMethodError(method, name, shortName));
      }
    };
  }
}
