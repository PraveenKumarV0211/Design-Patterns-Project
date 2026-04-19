package com.example.demo.pattern.template;

public abstract class CoverLetterPromptTemplate {

    // Template method — defines the fixed algorithm skeleton
    public final String buildPrompt(String resumeText, String companyName, String roleName, String jobType, String notes) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(getPersonalization(companyName, roleName, jobType));
        prompt.append("\n\n");
        prompt.append(getResumeContext(resumeText));
        if (notes != null && !notes.isBlank()) {
            prompt.append("\n\n");
            prompt.append(getAdditionalContext(notes));
        }
        prompt.append("\n\n");
        prompt.append(getOutputInstructions());
        return prompt.toString();
    }

    protected abstract String getPersonalization(String companyName, String roleName, String jobType);
    protected abstract String getResumeContext(String resumeText);
    protected abstract String getOutputInstructions();

    protected String getAdditionalContext(String notes) {
        return "Additional notes about the role: " + notes;
    }
}
