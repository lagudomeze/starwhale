/*
 * Copyright 2022.1-2022
 * StarWhale.ai All right reserved. This software is the confidential and proprietary information of
 * StarWhale.ai ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with StarWhale.ai.
 */

package ai.starwhale.mlops.common;

import ai.starwhale.mlops.exception.ConvertException;

public interface Convertor<T, R> {

    R convert(T t) throws ConvertException;

    T revert(R r) throws ConvertException;

}
