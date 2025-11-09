export default function PostCard({ post, onComment }) {
	return (
		<div className="bg-white rounded-xl shadow p-4">
			<h3 className="font-semibold">{post.author}</h3>
			<p className="mt-2">{post.content}</p>
			<div className="mt-3 flex justify-between text-sm text-gray-500">
				<button onClick={onComment}>💬 Comments</button>
				<span>{post.likes} ❤️</span>
			</div>
		</div>
	);
}
