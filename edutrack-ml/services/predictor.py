# services/predictor.py

import numpy as np
from config import Config

def predict_next_score(percentages):
    """
    Predict next quiz score using Linear Regression
    """
    if len(percentages) < Config.MIN_ATTEMPTS_FOR_PREDICTION:
        return {
            "predictedScore": None,
            "message": "Need at least 2 attempts for prediction",
            "trend": "UNKNOWN"
        }

    # create X (attempt numbers) and y (percentages)
    X = np.array(range(len(percentages))).reshape(-1, 1)
    y = np.array(percentages)

    # simple linear regression manually
    n      = len(X)
    sum_x  = np.sum(X)
    sum_y  = np.sum(y)
    sum_xy = np.sum(X.flatten() * y)
    sum_x2 = np.sum(X.flatten() ** 2)

    # calculate slope and intercept
    denominator = (n * sum_x2 - sum_x ** 2)
    if denominator == 0:
        slope = 0
    else:
        slope = (n * sum_xy - sum_x * sum_y) / denominator

    intercept = (sum_y - slope * sum_x) / n

    # predict next score
    next_x         = len(percentages)
    predicted      = float(slope * next_x + intercept)
    predicted      = max(0, min(100, round(predicted, 2)))

    # determine trend
    if slope > 2:
        trend = "IMPROVING"
    elif slope < -2:
        trend = "DECLINING"
    else:
        trend = "STABLE"

    return {
        "predictedScore": predicted,
        "trend"         : trend,
        "improvementRate": round(float(slope), 2),
        "message": f"Based on your {len(percentages)} attempts"
    }

def predict_dropout_risk(processed_data):
    """
    Predict dropout risk using rule-based Decision Tree logic
    """
    risk_score    = 0
    risk_factors  = []

    days_inactive = processed_data.get("lastAttempt", 0)
    progress      = processed_data.get("daysActive", 0)
    total_attempts= processed_data.get("totalAttempts", 0)
    percentages   = processed_data.get("percentages", [])

    # factor 1 — days since last attempt
    if days_inactive >= Config.HIGH_RISK_DAYS_INACTIVE:
        risk_score += 40
        risk_factors.append(
            f"No activity for {days_inactive} days")
    elif days_inactive >= Config.MEDIUM_RISK_DAYS_INACTIVE:
        risk_score += 20
        risk_factors.append("Low activity recently")

    # factor 2 — low attempts
    if total_attempts < 2:
        risk_score += 30
        risk_factors.append("Very few quiz attempts")
    elif total_attempts < 5:
        risk_score += 10

    # factor 3 — declining scores
    if len(percentages) >= 3:
        recent = percentages[-3:]
        if recent[-1] < recent[0]:
            risk_score += 20
            risk_factors.append("Scores are declining")

    # factor 4 — consistently low scores
    if percentages:
        avg = sum(percentages) / len(percentages)
        if avg < 40:
            risk_score += 10
            risk_factors.append("Consistently low scores")

    # determine risk level
    if risk_score >= 50:
        risk_level = "HIGH"
    elif risk_score >= 25:
        risk_level = "MEDIUM"
    else:
        risk_level = "LOW"

    return {
        "riskLevel"  : risk_level,
        "riskScore"  : risk_score,
        "riskFactors": risk_factors
    }

def generate_suggestions(processed_data, subject_classification,
                          prediction, dropout_risk):
    """
    Generate personalised suggestions based on all predictions
    """
    suggestions = []
    percentages = processed_data.get("percentages", [])
    avg = sum(percentages) / len(percentages) if percentages else 0

    # overall performance
    if avg >= 75:
        suggestions.append(
            "Excellent performance! You are in the top tier.")
    elif avg >= 50:
        suggestions.append(
            "Good performance! A little more focus will make you excellent.")
    else:
        suggestions.append(
            "Keep going! Revisit course materials and practice more.")

    # trend based
    trend = prediction.get("trend", "UNKNOWN")
    if trend == "IMPROVING":
        suggestions.append(
            "Great progress! Your scores are consistently improving.")
    elif trend == "DECLINING":
        suggestions.append(
            "Your scores are declining. Take a break and revise fundamentals.")

    # weak area suggestions
    for subject in subject_classification.get("weak", []):
        suggestions.append(
            f"Focus on {subject} — your average is below 50%.")

    # strong area encouragement
    for subject in subject_classification.get("strong", []):
        suggestions.append(
            f"Outstanding in {subject}! Consider advanced topics.")

    # dropout risk warning
    if dropout_risk.get("riskLevel") == "HIGH":
        suggestions.append(
            "You have been inactive. Come back and attempt a quiz today!")
    elif dropout_risk.get("riskLevel") == "MEDIUM":
        suggestions.append(
            "Try to attempt at least one quiz every 2-3 days.")

    return suggestions