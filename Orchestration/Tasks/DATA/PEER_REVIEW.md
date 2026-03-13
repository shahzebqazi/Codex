# Peer Review Framework

Structured review for code and documents. Replaces ad-hoc "code review" concept.

## Components

### Summary
- One paragraph overview of the work and review scope.

### Strengths
- What works well. Notable positives.

### Weaknesses
- Gaps, risks, or concerns.

### Detailed Feedback by Criterion

| Criterion | Notes |
|-----------|-------|
| Theoretical soundness | Correctness of approach, assumptions |
| Novelty | Originality, prior art |
| Methodology | Rigor, reproducibility |
| Clarity | Readability, structure |
| Impact | Significance, applicability |
| Ethics | Safety, bias, fairness |

### Overall Recommendation

- **Accept** / **Accept with minor revisions** / **Major revisions** / **Reject**
- Confidence: Low / Medium / High

### Confidential Comments

- For reviewers/editors only. Not shared with author. Use for sensitive feedback.

## Usage

- Code review: apply to PRs, patches, snippets.
- Document review: apply to papers, specs, RFCs.
- Output as Markdown to `.ai/reviews/` or inline in PR description.
