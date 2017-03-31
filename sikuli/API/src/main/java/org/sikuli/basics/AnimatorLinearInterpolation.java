/*
 * Copyright (c) 2010-2016, Sikuli.org, sikulix.com
 * Released under the MIT License.
 *
 */

package org.sikuli.basics;

public class AnimatorLinearInterpolation extends AnimatorTimeValueFunction {

  float _stepUnit;

  public AnimatorLinearInterpolation(float beginVal, float endVal, long totalTime) {
    super(beginVal, endVal, totalTime);
    _stepUnit = (endVal - beginVal) / (float) totalTime;
  }

  @Override
  public float getValue(long t) {
    if (t > _totalTime) {
      return _endVal;
    }
    return _beginVal + _stepUnit * t;
  }
}
