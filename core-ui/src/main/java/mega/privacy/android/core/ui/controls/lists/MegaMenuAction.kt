package mega.privacy.android.core.ui.controls.lists

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import mega.privacy.android.core.R
import mega.privacy.android.core.ui.controls.controlssliders.MegaSwitch
import mega.privacy.android.core.ui.preview.BooleanProvider
import mega.privacy.android.core.ui.preview.CombinedThemePreviews
import mega.privacy.android.core.ui.theme.AndroidTheme
import mega.privacy.android.core.ui.theme.extensions.conditional
import mega.privacy.android.core.ui.theme.extensions.red_600_red_300
import mega.privacy.android.core.ui.theme.extensions.textColorPrimary
import mega.privacy.android.core.ui.theme.extensions.textColorSecondary

/**
 * MegaMenuAction
 *
 * One line list menu item from Figma Designs
 * @param text list item title
 * @param icon list icon if null the list will not be having prefix icon
 * @param addIconPadding should be true if we need to leave icon space before text
 * @param hasSwitch true if list item has switch
 * @param isChecked sets the default switch value
 * @param isDestructive if true the text will be displayed in destructive(red) colors
 * @param addSeparator if true divider will be drawn below the item
 * @param dividerPadding padding start for the divider below menu action
 * @param onActionClicked trigger item click on menu action
 * @param modifier
 */
@Composable
fun MegaMenuAction(
    text: String,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    addIconPadding: Boolean = true,
    isDestructive: Boolean = false,
    hasSwitch: Boolean = false,
    isChecked: Boolean = false,
    addSeparator: Boolean = true,
    dividerPadding: Dp = 72.dp,
    onActionClicked: (() -> Unit)? = null,
) {
    Column {
        Row(
            modifier = modifier
                .height(56.dp)
                .fillMaxWidth()
                .conditional(onActionClicked != null) {
                    clickable {
                        onActionClicked?.invoke()
                    }
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                Icon(
                    modifier = Modifier
                        .testTag(MENU_ITEM_ICON_TAG)
                        .padding(end = 16.dp)
                        .size(size = 24.dp),
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = if (isDestructive) MaterialTheme.colors.red_600_red_300 else MaterialTheme.colors.textColorSecondary,
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier
                        .testTag(MENU_ITEM_TEXT_TAG)
                        .conditional(icon == null && addIconPadding) {
                            padding(start = 56.dp)
                        },
                    text = text,
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = if (isDestructive) MaterialTheme.colors.red_600_red_300 else MaterialTheme.colors.textColorPrimary,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (hasSwitch) {
                MegaSwitch(
                    modifier = Modifier.testTag(MENU_ITEM_SWITCH_TAG),
                    checked = isChecked,
                    onCheckedChange = {
                        onActionClicked?.invoke()
                    }
                )
            }
        }
        if (addSeparator) {
            Divider(modifier = Modifier.padding(start = dividerPadding))
        }
    }
}

internal const val MENU_ITEM_ICON_TAG = "menu_list_view_item:list_icon:"
internal const val MENU_ITEM_TEXT_TAG = "menu_list_view_item:text_title"
internal const val MENU_ITEM_SWITCH_TAG = "menu_list_view_item:button_switch"

@CombinedThemePreviews
@Composable
private fun PreviewPreviewMegaMenuAction(
    @PreviewParameter(BooleanProvider::class) isDestructive: Boolean,
) {
    AndroidTheme(isDark = isSystemInDarkTheme()) {
        MegaMenuAction(
            text = "Menu Item",
            icon = R.drawable.ic_folder_list,
            isDestructive = isDestructive,
        )
    }
}

@CombinedThemePreviews
@Composable
private fun PreviewMegaMenuActionWithSwitch(
    @PreviewParameter(BooleanProvider::class) hasSwitch: Boolean,
) {
    AndroidTheme(isDark = isSystemInDarkTheme()) {
        MegaMenuAction(
            text = "Menu Item",
            icon = R.drawable.ic_folder_list,
            hasSwitch = hasSwitch,
        )
    }
}

@CombinedThemePreviews
@Composable
private fun PreviewMegaMenuActionWithoutIcon(
    @PreviewParameter(BooleanProvider::class) hasSwitch: Boolean,
) {
    AndroidTheme(isDark = isSystemInDarkTheme()) {
        MegaMenuAction(
            text = "Menu Item",
            hasSwitch = hasSwitch,
        )
    }
}