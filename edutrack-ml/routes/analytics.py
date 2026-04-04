# routes/analytics.py

from flask import Blueprint, request, jsonify
from services.data_processor import process_student_data, process_subject_data
from services.predictor import (predict_next_score, predict_dropout_risk,
                                 generate_suggestions)
from utils.helpers import validate_required_fields

analytics_bp = Blueprint("analytics", __name__)

@analytics_bp.route("/health", methods=["GET"])
def health():
    """Check if ML service is running"""
    return jsonify({
        "status" : "UP",
        "service": "EduTrack ML Service",
        "version": "1.0"
    }), 200

@analytics_bp.route("/predict", methods=["POST"])
def predict():
    """Main prediction endpoint — called by Spring Boot"""
    data = request.get_json()
    if not data:
        return jsonify({"error": "No data provided"}), 400

    missing = validate_required_fields(data, ["studentId", "scores"])
    if missing:
        return jsonify({
            "error": f"Missing fields: {missing}"}), 400

    # process data
    processed        = process_student_data(data)
    subject_class    = process_subject_data(
                           processed["subjectAverages"])
    prediction       = predict_next_score(processed["percentages"])
    dropout_risk     = predict_dropout_risk(processed)
    suggestions      = generate_suggestions(
                           processed, subject_class,
                           prediction, dropout_risk)

    return jsonify({
        "studentId"            : processed["studentId"],
        "totalAttempts"        : processed["totalAttempts"],
        "averagePercentage"    : round(
            sum(processed["percentages"]) /
            len(processed["percentages"]), 2)
            if processed["percentages"] else 0,
        "subjectPerformance"   : {
            "strong" : subject_class["strong"],
            "average": subject_class["average"],
            "weak"   : subject_class["weak"]
        },
        "prediction"           : prediction,
        "dropoutRisk"          : dropout_risk,
        "suggestions"          : suggestions
    }), 200

@analytics_bp.route("/weak-areas", methods=["POST"])
def weak_areas():
    """Get weak subject areas for a student"""
    data = request.get_json()
    if not data:
        return jsonify({"error": "No data provided"}), 400

    processed     = process_student_data(data)
    subject_class = process_subject_data(
                        processed["subjectAverages"])

    return jsonify({
        "studentId"         : processed["studentId"],
        "weakAreas"         : subject_class["weak"],
        "averageAreas"      : subject_class["average"],
        "strongAreas"       : subject_class["strong"],
        "subjectAverages"   : processed["subjectAverages"]
    }), 200

@analytics_bp.route("/suggestions", methods=["POST"])
def suggestions():
    """Get AI-powered suggestions for a student"""
    data = request.get_json()
    if not data:
        return jsonify({"error": "No data provided"}), 400

    processed     = process_student_data(data)
    subject_class = process_subject_data(
                        processed["subjectAverages"])
    prediction    = predict_next_score(processed["percentages"])
    dropout_risk  = predict_dropout_risk(processed)
    result        = generate_suggestions(
                        processed, subject_class,
                        prediction, dropout_risk)

    return jsonify({
        "studentId"  : processed["studentId"],
        "suggestions": result
    }), 200