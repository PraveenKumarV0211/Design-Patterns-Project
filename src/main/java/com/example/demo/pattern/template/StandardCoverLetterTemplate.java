package com.example.demo.pattern.template;

import org.springframework.stereotype.Component;

@Component
public class StandardCoverLetterTemplate extends CoverLetterPromptTemplate {

    @Override
    protected String getPersonalization(String companyName, String roleName, String jobType) {
        return String.format(
            "Generate a professional cover letter for the following job application:\n" +
            "- Company: %s\n- Role: %s\n- Job Type: %s",
            companyName, roleName, jobType
        );
    }

    @Override
    protected String getResumeContext(String resumeText) {
        return "Here is the applicant's resume to use as context:\n\n" + resumeText;
    }

    @Override
    protected String getOutputInstructions() {
        return "Write a compelling, personalized cover letter of 3-4 paragraphs. " +
               "Include a strong opening tailored to the company, highlight the most relevant " +
               "experience and skills from the resume, demonstrate genuine enthusiasm for the role, " +
               "and close with a confident call to action. " +
               "IMPORTANT FORMATTING RULES: " +
               "- Output ONLY the body of the letter (no title, no 'Cover Letter' heading, no subject line). " +
               "- Start directly with 'Dear [Hiring Team],' or similar salutation. " +
               "- Do NOT use any markdown: no **, no __, no ##, no ---, no bullet points. " +
               "- Write in plain flowing prose, natural and human-sounding. " +
               "- Separate each paragraph with a blank line. " +
               "- End with a sign-off like 'Sincerely,' followed by the applicant's name on the next line.";
    }
}
