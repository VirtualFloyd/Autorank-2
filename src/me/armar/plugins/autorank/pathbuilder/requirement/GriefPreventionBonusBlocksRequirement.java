package me.armar.plugins.autorank.pathbuilder.requirement;

import me.armar.plugins.autorank.language.Lang;
import me.staartvin.utils.pluginlibrary.Library;
import me.staartvin.utils.pluginlibrary.hooks.GriefPreventionHook;

import java.util.UUID;

public class GriefPreventionBonusBlocksRequirement extends AbstractRequirement {

    int bonusBlocks = -1;
    private GriefPreventionHook handler = null;

    @Override
    public String getDescription() {
        return Lang.GRIEF_PREVENTION_BONUS_BLOCKS_REQUIREMENT.getConfigValue(bonusBlocks);
    }

    @Override
    public String getProgressString(UUID uuid) {
        return handler.getNumberOfBonusBlocks(uuid) + "/" + bonusBlocks;
    }

    @Override
    protected boolean meetsRequirement(UUID uuid) {

        if (!handler.isHooked())
            return false;

        return handler.getNumberOfBonusBlocks(uuid) >= bonusBlocks;
    }

    @Override
    public boolean initRequirement(final String[] options) {

        // Add dependency
        addDependency(Library.GRIEFPREVENTION);

        handler = (GriefPreventionHook) this.getDependencyManager()
                .getLibraryHook(Library.GRIEFPREVENTION).orElse(null);

        if (options.length > 0) {
            try {
                bonusBlocks = Integer.parseInt(options[0]);
            } catch (NumberFormatException e) {
                this.registerWarningMessage("An invalid number is provided");
                return false;
            }
        }

        if (bonusBlocks < 0) {
            this.registerWarningMessage("No number is provided or smaller than 0.");
            return false;
        }

        if (handler == null || !handler.isHooked()) {
            this.registerWarningMessage("GriefPrevention is not available");
            return false;
        }

        return true;
    }

    @Override
    public double getProgressPercentage(UUID uuid) {
        return handler.getNumberOfBonusBlocks(uuid) * 1.0d / bonusBlocks;
    }
}
