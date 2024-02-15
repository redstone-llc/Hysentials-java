package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;

import llc.redstone.hysentials.htsl.PotionEffect;
import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.PotionEffect;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class Conditional extends Loader {
    public Conditional(List<Object[]> conditions, boolean matchAnyCondition, List<Object[]> ifActions, List<Object[]> elseActions) {
        super("Conditional", "condition", conditions, matchAnyCondition, ifActions, elseActions);

        if (conditions != null) {
            add(LoaderObject.click(10));
            conditions.forEach(c -> {
                sequence.addAll(loadCondition(c));
            });
            add(LoaderObject.back());
        }

        if (matchAnyCondition) {
            add(LoaderObject.click(11));
        }

        if (ifActions != null && ifActions.size() > 0) {
            add(LoaderObject.click(12));
            for (Object[] action : ifActions) {
                sequence.addAll(loadAction(action));
            }
            add(LoaderObject.back());
        }

        if (elseActions != null && elseActions.size() > 0) {
            add(LoaderObject.click(13));
            for (Object[] action : elseActions) {
                sequence.addAll(loadAction(action));
            }
            add(LoaderObject.back());
        }
    }


    public List<LoaderObject> loadCondition(Object[] condition) {
        List<LoaderObject> sequence = new ArrayList<>();
        String conditionType = condition[0].toString();
        Object[] conditionData = (Object[]) condition[1];

        sequence.add(LoaderObject.click(50));
        switch (conditionType) {
            case "has_potion_effect": {
                sequence.add(LoaderObject.setGuiContext("Condition -> Has Potion Effect"));
                sequence.add(LoaderObject.option("Has Potion Effect"));
                if (conditionData[0] != null) {
                    sequence.add(LoaderObject.click(10));
                    PotionEffect effect = PotionEffect.fromString(conditionData[0].toString());
                    if (effect.isOnSecondPage()) {
                        sequence.add(LoaderObject.click(53));
                    }
                    sequence.add(LoaderObject.click(effect.getSlot()));
                }
                sequence.add(LoaderObject.back());
                break;
            }
            case "doing_parkour": {
                sequence.add(LoaderObject.option("Doing Parkour"));
                break;
            }
            case "has_item": {
                sequence.add(LoaderObject.setGuiContext("Condition -> Has Item"));
                sequence.add(LoaderObject.option("Has Item"));
                if (conditionData[0] != null) {
                    sequence.add(LoaderObject.click(10));
                    sequence.add(LoaderObject.item(conditionData[0].toString()));
                }
                if (conditionData[1].toString().equals("item_type")) {
                    sequence.add(LoaderObject.click(11));
                    sequence.add(LoaderObject.click(10));
                }
                if (conditionData[2] != null && !conditionData[2].toString().equals("anywhere")) {
                    sequence.add(LoaderObject.click(12));
                    switch (conditionData[2].toString()) {
                        case "Hand": {
                            sequence.add(LoaderObject.click(10));
                            break;
                        }
                        case "Armor": {
                            sequence.add(LoaderObject.click(11));
                            break;
                        }
                        case "Hotbar": {
                            sequence.add(LoaderObject.click(12));
                            break;
                        }
                        case "Inventory": {
                            sequence.add(LoaderObject.click(13));
                            break;
                        }
                    }
                }

                if ((boolean) conditionData[3]) {
                    sequence.add(LoaderObject.click(13));
                    sequence.add(LoaderObject.click(11));
                }
                sequence.add(LoaderObject.back());
                break;
            }
            case "within_region": {
                sequence.add(LoaderObject.setGuiContext("Condition -> Within Region"));
                sequence.add(LoaderObject.option("Within Region"));
                if (conditionData[0] != null) {
                    sequence.add(LoaderObject.click(10));
                    sequence.add(LoaderObject.option(conditionData[0].toString()));
                }
                sequence.add(LoaderObject.back());
                break;
            }
            case "required_permission": {
                sequence.add(LoaderObject.setGuiContext("Condition -> Required Permission"));
                sequence.add(LoaderObject.option("Required Permission"));
                if (conditionData[0] != null) {
                    sequence.add(LoaderObject.click(10));
                    sequence.add(LoaderObject.option(conditionData[0].toString()));
                }
                sequence.add(LoaderObject.back());
                break;
            }
            case "player_stat_requirement": {
                sequence.add(LoaderObject.option("Player Stat Requirement"));
                if (conditionData[0] != null && !conditionData[0].toString().equals("Kills")) {
                    sequence.add(LoaderObject.click(10));
                    sequence.add(LoaderObject.chat(conditionData[0].toString()));
                }
                if (conditionData[1] != null && !conditionData[1].equals("equal_to")) {
                    sequence.add(LoaderObject.click(11));
                    switch (conditionData[1].toString()) {
                        case "less_than": {
                            sequence.add(LoaderObject.click(10));
                            break;
                        }
                        case "less_than_or_equal_to": {
                            sequence.add(LoaderObject.click(11));
                            break;
                        }
                        case "greater_than": {
                            sequence.add(LoaderObject.click(14));
                            break;
                        }
                        case "greater_than_or_equal_to": {
                            sequence.add(LoaderObject.click(13));
                            break;
                        }
                    }
                }

                if (conditionData[2] != null) {
                    sequence.add(LoaderObject.click(12));
                    sequence.add(LoaderObject.anvil(conditionData[2].toString()));
                }
                sequence.add(LoaderObject.back());
                break;
            }
            case "global_stat_requirement": {
                sequence.add(LoaderObject.option("Global Stat Requirement"));
                if (conditionData[0] != null && !conditionData[0].toString().equals("Kills")) {
                    sequence.add(LoaderObject.click(10));
                    sequence.add(LoaderObject.chat(conditionData[0].toString()));
                }
                if (conditionData[1] != null && !conditionData[1].toString().equals("equal_to")) {
                    sequence.add(LoaderObject.click(11));
                    switch (conditionData[1].toString()) {
                        case "less_than": {
                            sequence.add(LoaderObject.click(10));
                            break;
                        }
                        case "less_than_or_equal_to": {
                            sequence.add(LoaderObject.click(11));
                            break;
                        }
                        case "greater_than": {
                            sequence.add(LoaderObject.click(14));
                            break;
                        }
                        case "greater_than_or_equal_to": {
                            sequence.add(LoaderObject.click(13));
                            break;
                        }
                    }
                }

                if (conditionData[2] != null) {
                    sequence.add(LoaderObject.click(12));
                    sequence.add(LoaderObject.anvil(conditionData[2].toString()));
                }
                sequence.add(LoaderObject.back());
                break;
            }
            case "team_stat_requirement": {
                sequence.add(LoaderObject.option("Team Stat Requirement"));
                if (conditionData[0] != null && !conditionData[0].toString().equals("Kills")) {
                    sequence.add(LoaderObject.click(10));
                    sequence.add(LoaderObject.chat(conditionData[0].toString()));
                }
                if (conditionData[1] != null && !conditionData[0].toString().equalsIgnoreCase("none")) {
                    sequence.add(LoaderObject.click(11));
                    sequence.add(LoaderObject.option(conditionData[1].toString()));
                }
                if (conditionData[2] != null && !conditionData[2].equals("equal_to")) {
                    sequence.add(LoaderObject.click(12));
                    switch (conditionData[2].toString()) {
                        case "less_than": {
                            sequence.add(LoaderObject.click(10));
                            break;
                        }
                        case "less_than_or_equal_to": {
                            sequence.add(LoaderObject.click(11));
                            break;
                        }
                        case "greater_than": {
                            sequence.add(LoaderObject.click(14));
                            break;
                        }
                        case "greater_than_or_equal_to": {
                            sequence.add(LoaderObject.click(13));
                            break;
                        }
                    }
                }

                if (conditionData[3] != null) {
                    sequence.add(LoaderObject.click(13));
                    sequence.add(LoaderObject.anvil(conditionData[3].toString()));
                }
                sequence.add(LoaderObject.back());
                break;
            }
            case "required_group": {
                sequence.add(LoaderObject.setGuiContext("Condition -> Required Group"));
                sequence.add(LoaderObject.option("Required Group"));
                if (conditionData[0] != null) {
                    sequence.add(LoaderObject.click(10));
                    sequence.add(LoaderObject.option(conditionData[0].toString()));
                }
                if ((boolean) conditionData[1]) {
                    sequence.add(LoaderObject.click(11));
                }
                sequence.add(LoaderObject.back());
                break;
            }
            case "required_team": {
                sequence.add(LoaderObject.setGuiContext("Condition -> Required Team"));
                sequence.add(LoaderObject.option("Required Team"));
                if (conditionData[0] != null) {
                    sequence.add(LoaderObject.click(10));
                    sequence.add(LoaderObject.option(conditionData[0].toString()));
                }
                sequence.add(LoaderObject.back());
                break;
            }
            case "is_flying": {
                sequence.add(LoaderObject.option("Player Flying"));
                break;
            }
            case "is_sneaking": {
                sequence.add(LoaderObject.option("Player Sneaking"));
                break;
            }
            case "placeholder_stat_requirement": {
                sequence.add(LoaderObject.option("Placeholder Number Requirement"));
                if (conditionData[0] != null && !conditionData[0].toString().equals("Kills")) {
                    sequence.add(LoaderObject.click(10));
                    sequence.add(LoaderObject.anvil(conditionData[0].toString()));
                }
                if (conditionData[1] != null && !conditionData[1].toString().equals("equal_to")) {
                    sequence.add(LoaderObject.click(11));
                    switch (conditionData[1].toString()) {
                        case "less_than": {
                            sequence.add(LoaderObject.click(10));
                            break;
                        }
                        case "less_than_or_equal_to": {
                            sequence.add(LoaderObject.click(11));
                            break;
                        }
                        case "greater_than": {
                            sequence.add(LoaderObject.click(14));
                            break;
                        }
                        case "greater_than_or_equal_to": {
                            sequence.add(LoaderObject.click(13));
                            break;
                        }
                    }
                }

                if (conditionData[2] != null) {
                    sequence.add(LoaderObject.click(12));
                    sequence.add(LoaderObject.anvil(conditionData[2].toString()));
                }
                sequence.add(LoaderObject.back());
                break;
            }
            case "required_gamemode": {
                sequence.add(LoaderObject.option("Required Gamemode"));
                if (conditionData[0] != null) {
                    sequence.add(LoaderObject.click(10));
                    switch (conditionData[0].toString()) {
                        case "survival": {
                            sequence.add(LoaderObject.option("Survival"));
                            break;
                        }
                        case "creative": {
                            sequence.add(LoaderObject.option("Creative"));
                            break;
                        }
                        case "adventure": {
                            sequence.add(LoaderObject.option("Adventure"));
                            break;
                        }
                    }
                }
                sequence.add(LoaderObject.back());
                break;
            }
            case "player_health": {
                sequence.add(LoaderObject.option("Player Health"));
                if (conditionData[0] != null && !conditionData[0].toString().equals("equal_to")) {
                    sequence.add(LoaderObject.click(10));
                    switch (conditionData[0].toString()) {
                        case "less_than": {
                            sequence.add(LoaderObject.click(10));
                            break;
                        }
                        case "less_than_or_equal_to": {
                            sequence.add(LoaderObject.click(11));
                            break;
                        }
                        case "greater_than": {
                            sequence.add(LoaderObject.click(14));
                            break;
                        }
                        case "greater_than_or_equal_to": {
                            sequence.add(LoaderObject.click(13));
                            break;
                        }
                    }
                }

                if (conditionData[1] != null) {
                    sequence.add(LoaderObject.click(11));
                    sequence.add(LoaderObject.anvil(conditionData[1].toString()));
                }
                sequence.add(LoaderObject.back());
                break;
            }
            case "max_player_health": {
                sequence.add(LoaderObject.option("Max Player Health"));
                if (conditionData[0] != null && !conditionData[0].toString().equals("equal_to")) {
                    sequence.add(LoaderObject.click(10));
                    switch (conditionData[0].toString()) {
                        case "less_than": {
                            sequence.add(LoaderObject.click(10));
                            break;
                        }
                        case "less_than_or_equal_to": {
                            sequence.add(LoaderObject.click(11));
                            break;
                        }
                        case "greater_than": {
                            sequence.add(LoaderObject.click(14));
                            break;
                        }
                        case "greater_than_or_equal_to": {
                            sequence.add(LoaderObject.click(13));
                            break;
                        }
                    }
                }

                if (conditionData[1] != null) {
                    sequence.add(LoaderObject.click(11));
                    sequence.add(LoaderObject.anvil(conditionData[1].toString()));
                }

                sequence.add(LoaderObject.back());
                break;
            }
            case "player_hunger": {
                sequence.add(LoaderObject.option("Player Hunger"));
                if (conditionData[0] != null && !conditionData[0].toString().equals("equal_to")) {
                    sequence.add(LoaderObject.click(10));
                    switch (conditionData[0].toString()) {
                        case "less_than": {
                            sequence.add(LoaderObject.click(10));
                            break;
                        }
                        case "less_than_or_equal_to": {
                            sequence.add(LoaderObject.click(11));
                            break;
                        }
                        case "greater_than": {
                            sequence.add(LoaderObject.click(14));
                            break;
                        }
                        case "greater_than_or_equal_to": {
                            sequence.add(LoaderObject.click(13));
                            break;
                        }
                    }
                }

                if (conditionData[1] != null) {
                    sequence.add(LoaderObject.click(11));
                    sequence.add(LoaderObject.anvil(conditionData[1].toString()));
                }
                sequence.add(LoaderObject.back());
                break;
            }
            case "damage_cause": {
                sequence.add(LoaderObject.setGuiContext("Condition -> Damage Cause"));
                sequence.add(LoaderObject.option("Damage Cause"));
                if (conditionData[0] != null) {
                    sequence.add(LoaderObject.click(10));
                    sequence.add(LoaderObject.option(conditionData[0].toString()));
                }
                sequence.add(LoaderObject.back());
                break;
            }
            case "block_type": {
                sequence.add(LoaderObject.setGuiContext("Condition -> Block Type"));
                sequence.add(LoaderObject.option("Block Type"));
                if (conditionData[0] != null) {
                    sequence.add(LoaderObject.click(10));
                    sequence.add(LoaderObject.option(conditionData[0].toString()));
                }
                if (conditionData[1] != null && (boolean) conditionData[1]) {
                    sequence.add(LoaderObject.click(11));
                }
                sequence.add(LoaderObject.back());
                break;
            }

        }

        return sequence;
    }

    @Override
    public String export(List<String> args) {
        args.get(0);
        return "conditional";
    }
}
