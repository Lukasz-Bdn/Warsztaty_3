package pl.coderslab.warsztat3.model;

public class SolutionWithAuthor {
	private SolutionDTO solution;
	private String authorName;
	
	public SolutionWithAuthor(SolutionDTO solution, String authorName) {
		super();
		this.solution = solution;
		this.authorName = authorName;
	}

	public SolutionDTO getSolution() {
		return solution;
	}

	public String getAuthorName() {
		return authorName;
	}
	
}