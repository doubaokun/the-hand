package com.gilt.thehand.rules

import com.gilt.thehand.{AbstractContext, Rule, RuleParser}

/**
 * Pass any number of rules into this And class to boolean-and their results together. This will short-circuit
 * processing once the first rule fails.
 */
case class And(values: Rule*) extends SeqRule {
  type InnerType = Rule

  def unapply(context: AbstractContext): Option[AbstractContext] = values.foldRight(Option(context)) ((currentRule, aggregateResult) =>
    if (aggregateResult.isDefined) currentRule.unapply(context)
    else aggregateResult
  )
}

/**
 * Parses a String to an And rule, recursively pasring for each member of the rule.
 */
object AndParser extends SeqRuleParser[And] {
  def toValue(value: String)(implicit parser: RuleParser): Rule = parser.fromString(value)

  def ruleConstructor = And.apply
}