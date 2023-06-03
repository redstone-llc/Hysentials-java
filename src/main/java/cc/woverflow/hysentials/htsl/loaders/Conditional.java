package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;

import cc.woverflow.hysentials.htsl.PotionEffect;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class Conditional extends Loader {
    public Conditional(List<Object[]> conditions, boolean matchAnyCondition, List<Object[]> ifActions, List<Object[]> elseActions) {
        super("Conditional", "condition", conditions, matchAnyCondition, ifActions, elseActions);

        if (conditions != null) {
            add(click(10));
            conditions.forEach(c -> {
                sequence.addAll(loadCondition(c));
            });
            add(back());
        }

        if (matchAnyCondition) {
            add(click(11));
        }

        if (ifActions != null && ifActions.size() > 0) {
            add(click(12));
            for (Object[] action : ifActions) {
                sequence.addAll(loadAction(action));
            }
            add(back());
        }

        if (elseActions != null && elseActions.size() > 0) {
            add(click(13));
            for (Object[] action : elseActions) {
                sequence.addAll(loadAction(action));
            }
            add(back());
        }
    }


    public List<LoaderObject> loadCondition(Object[] condition) {
        List<LoaderObject> sequence = new ArrayList<>();
        String conditionType = condition[0].toString();
        Object[] conditionData = (Object[]) condition[1];

        sequence.add(click(50));
        switch (conditionType) {
            case "has_potion_effect": {
                sequence.add(setGuiContext("Condition -> Has Potion Effect"));
                sequence.add(option("Has Potion Effect"));
                if (conditionData[0] != null) {
                    sequence.add(click(10));
                    PotionEffect effect = PotionEffect.fromString(conditionData[0].toString());
                    if (effect.isOnSecondPage()) {
                        sequence.add(click(53));
                    }
                    sequence.add(click(effect.getSlot()));
                }
                sequence.add(back());
                break;
            }
            case "doing_parkour": {
                sequence.add(option("Doing Parkour"));
                break;
            }
            case "has_item": {
                sequence.add(setGuiContext("Condition -> Has Item"));
                sequence.add(option("Has Item"));
                if (conditionData[0] != null) {
                    sequence.add(click(10));
                    sequence.add(item(conditionData[0].toString()));
                }
                if (conditionData[1].toString().equals("item_type")) {
                    sequence.add(click(11));
                    sequence.add(click(10));
                }
                if (conditionData[2] != null && !conditionData[2].toString().equals("anywhere")) {
                    sequence.add(click(12));
                    switch (conditionData[2].toString()) {
                        case "Hand": {
                            sequence.add(click(10));
                            break;
                        }
                        case "Armor": {
                            sequence.add(click(11));
                            break;
                        }
                        case "Hotbar": {
                            sequence.add(click(12));
                            break;
                        }
                        case "Inventory": {
                            sequence.add(click(13));
                            break;
                        }
                    }
                }

                if ((boolean) conditionData[3]) {
                    sequence.add(click(13));
                    sequence.add(click(11));
                }
                sequence.add(back());
                break;
            }
            case "within_region": {
                sequence.add(setGuiContext("Condition -> Within Region"));
                sequence.add(option("Within Region"));
                if (conditionData[0] != null) {
                    sequence.add(click(10));
                    sequence.add(option(conditionData[0].toString()));
                }
                sequence.add(back());
                break;
            }
            case "required_permission": {
                sequence.add(setGuiContext("Condition -> Required Permission"));
                sequence.add(option("Required Permission"));
                if (conditionData[0] != null) {
                    sequence.add(click(10));
                    sequence.add(option(conditionData[0].toString()));
                }
                sequence.add(back());
                break;
            }
            case "player_stat_requirement": {
                sequence.add(option("Player Stat Requirement"));
                if (conditionData[0] != null && !conditionData[0].toString().equals("Kills")) {
                    sequence.add(click(10));
                    sequence.add(chat(conditionData[0].toString()));
                }
                if (conditionData[1] != null && !conditionData[1].equals("equal_to")) {
                    sequence.add(click(11));
                    switch (conditionData[1].toString()) {
                        case "less_than": {
                            sequence.add(click(10));
                            break;
                        }
                        case "less_than_or_equal_to": {
                            sequence.add(click(11));
                            break;
                        }
                        case "greater_than": {
                            sequence.add(click(14));
                            break;
                        }
                        case "greater_than_or_equal_to": {
                            sequence.add(click(13));
                            break;
                        }
                    }
                }

                if (conditionData[2] != null) {
                    sequence.add(click(12));
                    sequence.add(anvil(conditionData[2].toString()));
                }
                sequence.add(back());
                break;
            }
            case "global_stat_requirement": {
                sequence.add(option("Global Stat Requirement"));
                if (conditionData[0] != null && !conditionData[0].toString().equals("Kills")) {
                    sequence.add(click(10));
                    sequence.add(chat(conditionData[0].toString()));
                }
                if (conditionData[1] != null && !conditionData[1].toString().equals("equal_to")) {
                    sequence.add(click(11));
                    switch (conditionData[1].toString()) {
                        case "less_than": {
                            sequence.add(click(10));
                            break;
                        }
                        case "less_than_or_equal_to": {
                            sequence.add(click(11));
                            break;
                        }
                        case "greater_than": {
                            sequence.add(click(14));
                            break;
                        }
                        case "greater_than_or_equal_to": {
                            sequence.add(click(13));
                            break;
                        }
                    }
                }

                if (conditionData[2] != null) {
                    sequence.add(click(12));
                    sequence.add(anvil(conditionData[2].toString()));
                }
                sequence.add(back());
                break;
            }
            case "required_group": {
                sequence.add(setGuiContext("Condition -> Required Group"));
                sequence.add(option("Required Group"));
                if (conditionData[0] != null) {
                    sequence.add(click(10));
                    sequence.add(option(conditionData[0].toString()));
                }
                if ((boolean) conditionData[1]) {
                    sequence.add(click(11));
                }
                sequence.add(back());
                break;
            }
            case "is_sneaking": {
                sequence.add(option("Player Sneaking"));
                break;
            }
            case "placeholder_stat_requirement": {
                sequence.add(option("Placeholder Number Requirement"));
                if (conditionData[0] != null && !conditionData[0].toString().equals("Kills")) {
                    sequence.add(click(10));
                    sequence.add(anvil(conditionData[0].toString()));
                }
                if (conditionData[1] != null && !conditionData[1].toString().equals("equal_to")) {
                    sequence.add(click(11));
                    switch (conditionData[1].toString()) {
                        case "less_than": {
                            sequence.add(click(10));
                            break;
                        }
                        case "less_than_or_equal_to": {
                            sequence.add(click(11));
                            break;
                        }
                        case "greater_than": {
                            sequence.add(click(14));
                            break;
                        }
                        case "greater_than_or_equal_to": {
                            sequence.add(click(13));
                            break;
                        }
                    }
                }

                if (conditionData[2] != null) {
                    sequence.add(click(12));
                    sequence.add(anvil(conditionData[2].toString()));
                }
                sequence.add(back());
                break;
            }
            case "required_gamemode": {
                sequence.add(option("Required Gamemode"));
                if (conditionData[0] != null) {
                    sequence.add(click(10));
                    switch (conditionData[0].toString()) {
                        case "survival": {
                            sequence.add(option("Survival"));
                            break;
                        }
                        case "creative": {
                            sequence.add(option("Creative"));
                            break;
                        }
                        case "adventure": {
                            sequence.add(option("Adventure"));
                            break;
                        }
                    }
                }
                sequence.add(back());
                break;
            }
            case "player_health": {
                sequence.add(option("Player Health"));
                if (conditionData[0] != null && !conditionData[0].toString().equals("equal_to")) {
                    sequence.add(click(10));
                    switch (conditionData[0].toString()) {
                        case "less_than": {
                            sequence.add(click(10));
                            break;
                        }
                        case "less_than_or_equal_to": {
                            sequence.add(click(11));
                            break;
                        }
                        case "greater_than": {
                            sequence.add(click(14));
                            break;
                        }
                        case "greater_than_or_equal_to": {
                            sequence.add(click(13));
                            break;
                        }
                    }
                }

                if (conditionData[1] != null) {
                    sequence.add(click(11));
                    sequence.add(anvil(conditionData[1].toString()));
                }
                sequence.add(back());
                break;
            }
            case "max_player_health": {
                sequence.add(option("Max Player Health"));
                if (conditionData[0] != null && !conditionData[0].toString().equals("equal_to")) {
                    sequence.add(click(10));
                    switch (conditionData[0].toString()) {
                        case "less_than": {
                            sequence.add(click(10));
                            break;
                        }
                        case "less_than_or_equal_to": {
                            sequence.add(click(11));
                            break;
                        }
                        case "greater_than": {
                            sequence.add(click(14));
                            break;
                        }
                        case "greater_than_or_equal_to": {
                            sequence.add(click(13));
                            break;
                        }
                    }
                }

                if (conditionData[1] != null) {
                    sequence.add(click(11));
                    sequence.add(anvil(conditionData[1].toString()));
                }

                sequence.add(back());
                break;
            }
            case "player_hunger": {
                sequence.add(option("Player Hunger"));
                if (conditionData[0] != null && !conditionData[0].toString().equals("equal_to")) {
                    sequence.add(click(10));
                    switch (conditionData[0].toString()) {
                        case "less_than": {
                            sequence.add(click(10));
                            break;
                        }
                        case "less_than_or_equal_to": {
                            sequence.add(click(11));
                            break;
                        }
                        case "greater_than": {
                            sequence.add(click(14));
                            break;
                        }
                        case "greater_than_or_equal_to": {
                            sequence.add(click(13));
                            break;
                        }
                    }
                }

                if (conditionData[1] != null) {
                    sequence.add(click(11));
                    sequence.add(anvil(conditionData[1].toString()));
                }
                sequence.add(back());
                break;
            }
            case "damage_cause": {
                sequence.add(setGuiContext("Condition -> Damage Cause"));
                sequence.add(option("Damage Cause"));
                if (conditionData[0] != null) {
                    sequence.add(click(10));
                    sequence.add(option(conditionData[0].toString()));
                }
                sequence.add(back());
                break;
            }
            case "block_type": {
                sequence.add(setGuiContext("Condition -> Block Type"));
                sequence.add(option("Block Type"));
                if (conditionData[0] != null) {
                    sequence.add(click(10));
                    sequence.add(option(conditionData[0].toString()));
                }
                if (conditionData[1] != null && (boolean) conditionData[1]) {
                    sequence.add(click(11));
                }
                sequence.add(back());
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
