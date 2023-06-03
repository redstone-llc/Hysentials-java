package cc.woverflow.hysentials.htsl.compiler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConditionCompiler {
    public static class ConditionResult {
        public List<Object[]> condition;
        public String error;

        public ConditionResult(List<Object[]> condition, String error) {
            this.condition = condition;
            this.error = error;
        }
    }

    public static ConditionResult compile(String arg, List<String> compileErrors) {
        List<Object[]> conditionList = new ArrayList<>();
        if (arg.startsWith("or ")) {
            arg = arg.substring(3);
        }
        if (arg.startsWith("and ")) {
            arg = arg.substring(4);
        }
        if (arg.startsWith("(")) {
            arg = arg.substring(1);
        }
        if (arg.endsWith(")")) {
            arg = arg.substring(0, arg.length() - 1);
        }
        String[] args = arg.split(",");

        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].trim();
        }

        for (int i = 0; i < args.length; i++) {
            List<String> argsList = Compiler.getArgs(args[i]);
            String condition = new String(argsList.get(0));
            argsList.remove(0);

            switch (condition) {
                case "stat": {
                    String mode = validComparator(argsList.get(1));
                    ConditionResult result = isNullComparator(conditionList, mode);
                    if (result != null) return result;
                    conditionList.add(new Object[]{"player_stat_requirement",
                        new Object[]{argsList.get(0), mode, argsList.get(2)}
                    });
                    break;
                }
                case "globalstat": {
                    String mode = validComparator(argsList.get(1));
                    ConditionResult result = isNullComparator(conditionList, mode);
                    if (result != null) return result;
                    conditionList.add(new Object[]{"global_stat_requirement",
                        new Object[]{argsList.get(0), mode, argsList.get(2)}
                    });
                    break;
                }
                case "hasPotion": {
                    conditionList.add(new Object[]{"has_potion_effect",
                        new Object[]{argsList.get(0)}
                    });
                    break;
                }
                case "doingParkour": {
                    conditionList.add(new Object[]{"doing_parkour", new Object[]{}});
                    break;
                }
                case "inRegion": {
                    conditionList.add(new Object[]{"within_region",
                        new Object[]{argsList.get(0)}
                    });
                    break;
                }
                case "hasPermission": {
                    conditionList.add(new Object[]{"required_permission",
                        new Object[]{argsList.get(0)}
                    });
                    break;
                }
                case "hasGroup": {
                    conditionList.add(new Object[]{"required_group",
                        new Object[]{argsList.get(0), Boolean.parseBoolean(argsList.get(1))}
                    });
                    break;
                }
                case "damageCause": {
                    conditionList.add(new Object[]{"damage_cause",
                        new Object[]{argsList.get(0)}
                    });
                    break;
                }
                case "placeholder": {
                    String mode = validComparator(argsList.get(1));
                    ConditionResult result = isNullComparator(conditionList, mode);
                    if (result != null) return result;
                    conditionList.add(new Object[]{"placeholder_requirement",
                        new Object[]{argsList.get(0), mode, argsList.get(2)}
                    });
                }
                case "gamemode": {
                    if (!Arrays.asList("adventure", "survival", "creative").contains(args[1])) {
                        compileErrors.add("&cUnknown gamemode type &e" + args[1]);
                        break;
                    }
                    conditionList.add(new Object[]{"required_gamemode",
                        new Object[]{argsList.get(0)}
                    });
                    break;
                }
                case "isSneaking": {
                    conditionList.add(new Object[]{"is_sneaking", new Object[]{}});
                    break;
                }
                case "hasItem": {
                    String item_type = (argsList.get(1).equals("item_type") ? "item_type" : "metadata");
                    boolean requireAmount = (argsList.get(3).equals("requireAmount"));
                    conditionList.add(new Object[]{"has_item",
                        new Object[]{argsList.get(0), item_type, argsList.get(2), requireAmount}
                    });
                    break;
                }
                case "health": {
                    String mode = validComparator(argsList.get(1));
                    ConditionResult result = isNullComparator(conditionList, mode);
                    if (result != null) return result;
                    conditionList.add(new Object[]{"health_requirement",
                        new Object[]{mode, argsList.get(2)}
                    });
                    break;
                }
                case "maxHealth": {
                    String mode = validComparator(argsList.get(1));
                    ConditionResult result = isNullComparator(conditionList, mode);
                    if (result != null) return result;
                    conditionList.add(new Object[]{"max_health_requirement",
                        new Object[]{mode, argsList.get(2)}
                    });
                    break;
                }
                case "hunger": {
                    String mode = validComparator(argsList.get(1));
                    ConditionResult result = isNullComparator(conditionList, mode);
                    if (result != null) return result;
                    conditionList.add(new Object[]{"hunger_requirement",
                        new Object[]{mode, argsList.get(2)}
                    });
                    break;
                }
                case "blockType": {
                    conditionList.add(new Object[]{"block_type",
                        new Object[]{argsList.get(0), Boolean.parseBoolean(argsList.get(1))}
                    });
                    break;
                }
            }
            if (conditionList.size() != i+1 && !(condition.equals(""))) {
                compileErrors.add("&cUnknown condition &e" + condition);
                return new ConditionResult(conditionList, compileErrors.get(0));
            }
        }

        return new ConditionResult(conditionList, compileErrors.size() > 0 ? compileErrors.get(0) : null);
    }

    public static String export(String name, List<String> args)  {
        switch (name) {
            case "Player Stat Requirement": {
                return "stat \"" + args.get(0) + "\" " + args.get(1) + " " + args.get(2);
            }
            case "Global Stat Requirement": {
                return "globalstat \"" + args.get(0) + "\" " + args.get(1) + " " + args.get(2);
            }
            case "Has Potion Effect": {
                return "hasPotion \"" + args.get(0) + "\"";
            }
            case "Doing Parkour": {
                return "doingParkour";
            }
            case "Within Region": {
                return "inRegion \"" + args.get(0) + "\"";
            }
            case "Required Permission": {
                return "hasPermission \"" + args.get(0) + "\"";
            }
            case "Required Group": {
                return "hasGroup \"" + args.get(0) + "\" " + args.get(1);
            }
            case "Damage Cause": {
                return "damageCause \"" + args.get(0) + "\"";
            }
            case "Block Type": {
                return "blockType \"" + args.get(0) + "\" " + args.get(1);
            }
            case "Player Sneaking": {
                return "isSneaking";
            }
            case "Required Gamemode": {
                return "gamemode \"" + args.get(0) + "\"";
            }
            case "Placeholder Number Requirement": {
                return "placeholder \"" + args.get(0) + "\" " + args.get(1) + " " + args.get(2);
            }
            case "Player Hunger": {
                return "hunger " + args.get(1) + " " + args.get(2);
            }
            case "Player Health": {
                return "health " + args.get(1) + " " + args.get(2);
            }
            case "Player Max Health": {
                return "maxHealth " + args.get(1) + " " + args.get(2);
            }
            default: {
                return "";
            }
        }
    }

    public static ConditionResult isNullComparator(List<Object[]> conditionList, String comparator) {
        if (comparator == null) {
            return new ConditionResult(conditionList, "&cUnknown compare operation &e" + comparator);
        }
        return null;
    }

    public static String validComparator(String comparator) {
        switch (comparator) {
            case "=":
            case "==":
                comparator = "equal_to";
                break;
            case "<":
                comparator = "less_than";
                break;
            case "<=":
            case "=<":
                comparator = "less_than_or_equal_to";
                break;
            case ">":
                comparator = "greater_than";
                break;
            case "=>":
            case ">=":
                comparator = "greater_than_or_equal_to";
                break;
            default:
                comparator = null;
                break;
        }
        return comparator;
    }

    public static String undoValidComparator(String comparator) {
        switch (comparator) {
            case "Equal":
                comparator = "==";
                break;
            case "Less Than":
                comparator = "<";
                break;
            case "Less Than or Equal":
                comparator = "<=";
                break;
            case "Greater Than":
                comparator = ">";
                break;
            case "Greater Than or Equal":
                comparator = ">=";
                break;
        }
        return comparator;
    }
}
