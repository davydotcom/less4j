package com.github.sommeri.less4j.core.ast;

import java.util.ArrayList;
import java.util.List;

import com.github.sommeri.less4j.core.parser.HiddenTokenAwareTree;
import com.github.sommeri.less4j.utils.ArraysUtils;

public class SimpleSelector extends SelectorPart implements Cloneable {

  private String elementName;
  private boolean isStar;
  //*.warning and .warning are equivalent http://www.w3.org/TR/css3-selectors/#universal-selector
  //relevant only if the selector is a star
  private boolean isEmptyForm = false;
  private List<ElementSubsequent> subsequent = new ArrayList<ElementSubsequent>();

  public SimpleSelector(HiddenTokenAwareTree token, String elementName, boolean isStar) {
    super(token);
    this.elementName = elementName;
    this.isStar = isStar;
  }

  public boolean isSingleClassSelector() {
    return isOnePurposeSelector(ASTCssNodeType.CSS_CLASS);
  }

  public boolean isSingleIdSelector() {
    return isOnePurposeSelector(ASTCssNodeType.ID_SELECTOR);
  }

  private boolean isOnePurposeSelector(ASTCssNodeType purpose) {
    if (elementName != null || !isEmptyForm() || subsequent == null || subsequent.size() != 1)
      return false;

    return subsequent.get(0).getType() == purpose;
  }

  public String getElementName() {
    return elementName;
  }

  public boolean isStar() {
    return isStar;
  }

  public boolean isEmptyForm() {
    return isEmptyForm;
  }

  public void setEmptyForm(boolean isEmptyForm) {
    this.isEmptyForm = isEmptyForm;
  }

  public void setElementName(String elementName) {
    this.elementName = elementName;
  }

  public void setStar(boolean isStar) {
    this.isStar = isStar;
  }

  public boolean hasElement() {
    return null != getElementName();
  }

  public List<ElementSubsequent> getSubsequent() {
    return subsequent;
  }

  public boolean hasSubsequent() {
    return !subsequent.isEmpty();
  }

  public void addSubsequent(ElementSubsequent subsequent) {
    this.subsequent.add(subsequent);
  }

  public void addSubsequent(List<ElementSubsequent> subsequent) {
    this.subsequent.addAll(subsequent);
  }

  @Override
  public List<ASTCssNode> getChilds() {
    return new ArrayList<ASTCssNode>(subsequent);
  }

  public ASTCssNodeType getType() {
    return ASTCssNodeType.SIMPLE_SELECTOR;
  }

  @Override
  public SimpleSelector clone() {
    SimpleSelector clone = (SimpleSelector) super.clone();
    clone.subsequent = ArraysUtils.deeplyClonedList(getSubsequent());
    clone.configureParentToAllChilds();
    return clone;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(isStar ? "*" : getElementName());
    builder.append(subsequent);
    return builder.toString();
  }

  public ElementSubsequent getLastSubsequent() {
    if (subsequent.isEmpty())
      return null;

    return subsequent.get(subsequent.size() - 1);
  }

  public void extendName(String extension) {
    //FIXME: handle star
    if (isStar) {
      isStar=false;
      setElementName("*" + extension);
    } else {
      setElementName(getElementName() + extension);
    }
  }

}
