import com.atlan.pkg.CustomPackage
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.widgets.DropDown

object PackageConfig : CustomPackage(
    "@csa/ernest-admin-export",
    "Ernest Admin Export",
    "Export key administration details to tenant",
    "https://assets.atlan.com/assets/ph-user-circle-gear-light.svg",
    "https://stuff.atlan.com",
    uiConfig = UIConfig(
        listOf(
            UIStep(
                title = "Scope",
                description = "What to include",
                inputs = mapOf(
                    "objects_to_include" to DropDown(
                        label = "Objects to include",
                        possibleValues = mapOf(
                            "users" to "Users",
                            "groups" to "Groups ",
                        ),
                        help = "Select the objects you want to include ",
                        multiSelect = true,
                        required = true,
                    ),
                ),
            ),
        ),
    ),
    containerImage = "ernestatlan/altan-integration-test:tagname",
    containerCommand = listOf("doit"),
    outputs = WorkflowOutputs(
        mapOf(
            "dubug-logs" to "/tmp/debug.log",
            "admin-export" to "/tmp/admin-export.xls",
        ),
    ),
    keywords = listOf("kotlin", "utility", "admin", "export"),
    preview = true,

) {
    @JvmStatic
    fun main(args: Array<String>) {
        generate(this, args)
    }
}
