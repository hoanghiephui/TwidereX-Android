/*
 *  Twidere X
 *
 *  Copyright (C) TwidereProject and Contributors
 * 
 *  This file is part of Twidere X.
 * 
 *  Twidere X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  Twidere X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Twidere X. If not, see <http://www.gnu.org/licenses/>.
 */
package com.twidere.twiderex.kmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource

actual class ResLoader {
    actual fun getString(
        res: StringResource,
        vararg args: Any
    ): String {
        return res.localized(args = args)
    }

    @Composable
    actual fun getSvg(res: FileResource): Painter {
        return painterResource(res.filePath)
    }

    @Composable
    actual fun getImage(res: ImageResource): Painter {
        return painterResource(res.filePath)
    }
}
