# services/data_processor.py

from utils.helpers import calculate_percentage, get_performance_level

def process_student_data(data):
    """
    Process raw student data from Spring Boot
    and prepare it for ML prediction
    """
    student_id   = data.get("studentId")
    scores       = data.get("scores", [])
    total_q      = data.get("totalQuestions", [])
    subjects     = data.get("subjects", [])
    days_active  = data.get("daysActive", 0)
    last_attempt = data.get("daysSinceLastAttempt", 0)

    # calculate percentages for each attempt
    percentages = []
    for i in range(len(scores)):
        total = total_q[i] if i < len(total_q) else 10
        pct   = calculate_percentage(scores[i], total)
        percentages.append(pct)

    # group scores by subject
    subject_scores = {}
    for i, subject in enumerate(subjects):
        if subject not in subject_scores:
            subject_scores[subject] = []
        if i < len(percentages):
            subject_scores[subject].append(percentages[i])

    # calculate per subject average
    subject_averages = {}
    for subject, pcts in subject_scores.items():
        subject_averages[subject] = round(
            sum(pcts) / len(pcts), 2) if pcts else 0.0

    return {
        "studentId"      : student_id,
        "scores"         : scores,
        "percentages"    : percentages,
        "subjects"       : subjects,
        "subjectAverages": subject_averages,
        "daysActive"     : days_active,
        "lastAttempt"    : last_attempt,
        "totalAttempts"  : len(scores)
    }

def process_subject_data(subject_averages):
    """
    Classify subjects into STRONG, AVERAGE, WEAK
    """
    result = {"strong": [], "average": [], "weak": []}

    for subject, avg in subject_averages.items():
        level = get_performance_level(avg)
        if level == "STRONG":
            result["strong"].append(subject)
        elif level == "AVERAGE":
            result["average"].append(subject)
        else:
            result["weak"].append(subject)

    return result